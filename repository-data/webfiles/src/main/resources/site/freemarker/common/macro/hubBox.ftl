<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro hubBox options metadata={} hiddenSchemaList=[]>
    <#if options??>
        <#assign hasMetaData = metadata?? && metadata?has_content/>
        <#assign itemscope = hasMetaData?then(metadata.itemscope,"") />
        <#assign itemName = hasMetaData?then(metadata.name,"") />
        <#assign itemDatePublished = hasMetaData?then(metadata.datePublished,"") />

        <article class="hub-box${(options.imagesection??)?then(' hub-box--with-icon', '')}" ${itemscope}>
            <#if hiddenSchemaList?? && hiddenSchemaList?has_content>
                <#list hiddenSchemaList as schema>
                    <#if schema.prop == 'keyword'>
                        <meta itemprop="keywords" content="${schema.value}"/>
                    <#else>
                        <span itemprop="${schema.prop}" class="is-hidden">${schema.value}</span>
                    </#if>
                </#list>
            </#if>

            <#if options.background??>
            <div class="hub-box__image" style="background-image: url('${options.background}');"></div>
            </#if>

            <#if (options.imagesection??) && options.imagesection.leadImage??>
                <div class="hub-box__icon">
                    <#assign alttext = options.imagesection.alttext?has_content?then(options.imagesection.alttext, "NHS Digital article ") />
                    <@hst.link hippobean=options.imagesection.leadImage.original fullyQualified=true var="leadImage" />
                    <img class="hub-box__icon-img" src="${leadImage}" alt="${alttext}" />
                </div>
            </#if>

            <div class="hub-box__contents">
                <#if options.colorbox?has_content>
                  <div class="colour-class-${options.colorbox}">${options.colorbox?upper_case}</div>
                </#if>

                <#if options.title??>
                    <#--shema:name-->
                    <h2 class="hub-box__title" ${(!options.link??)?then(itemName,"")}>
                        <#if options.link??>
                        <a class="hub-box__title-a" href="${options.link}" ${itemName}>
                        </#if>
                        ${options.title}
                        <#if options.link??>
                        </a>
                        </#if>
                    </h2>
                </#if>

                <#if options.date??>
                <#--shema:datePublished-->
                <span class="hub-box__meta" ${itemDatePublished}>${options.date}</span>
                </#if>

                <#if options.text??>
                <p class="hub-box__text">${options.text}</p>
                </#if>

                <#if options.htmlText??>
                    <div class="hub-box__text">
                        <@hst.html hippohtml=options.htmlText contentRewriter=gaContentRewriter />
                    </div>
                </#if>

                <#if options.relatedLinks?has_content>
                    <div class="hub-box__links">
                        <span class="hub-box__links-title">Related to:</span>
                        <ul class="hub-box__links-list">
                            <#list options.relatedLinks as link>
                                <li>
                                    <a class="hub-box__links-anchor" href="${link.url}">${link.title}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <#if options.types??>
                <ul class="tag-list">
                <#list options.types as type>
                    <li class="tag">${type}</li>
                </#list>
                </ul>
                </#if>
            </div>
        </article>
    </#if>
</#macro>
