package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.emptyList;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import io.swagger.codegen.v3.CodegenParameter;
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;
import uk.nhs.digital.apispecs.swagger.request.CodegenRequestParameterExampleRenderer;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ApiSpecificationStaticHtml2Codegen extends StaticHtml2Codegen {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecificationStaticHtml2Codegen.class);

    private CommonmarkMarkdownConverter markdownConverter = new CommonmarkMarkdownConverter();

    private CodegenRequestParameterExampleRenderer codegenRequestParameterExampleRenderer =
        new CodegenRequestParameterExampleRenderer(markdownConverter);

    @Override
    public void preprocessOpenAPI(OpenAPI openApi) {
        this.openAPI = openApi;

        if (openAPI.getInfo() != null) {
            Info info = openAPI.getInfo();
            if (StringUtils.isBlank(jsProjectName) && info.getTitle() != null) {
                // when jsProjectName is not specified, generate it from info.title
                jsProjectName = sanitizeName(dashize(info.getTitle()));
            }
        }

        // default values
        if (StringUtils.isBlank(jsProjectName)) {
            jsProjectName = "swagger-js-client";
        }
        if (StringUtils.isBlank(jsModuleName)) {
            jsModuleName = camelize(underscore(jsProjectName));
        }

        additionalProperties.put("jsProjectName", jsProjectName);
        additionalProperties.put("jsModuleName", jsModuleName);

        preparHtmlForGlobalDescription(openAPI);

        prepareHtmlForPathDescriptions(openAPI);

        prepareHtmlForPathParameters(openAPI);

        prepareHtmlForMethodsParameters(openAPI);
    }

    @Override
    public void setParameterExampleValue(final CodegenParameter parameter) {
        parameter.example = codegenRequestParameterExampleRenderer.htmlForExampleValueOf(parameter);
    }

    /**
     * Parse Markdown to HTML for the main "Description" attribute
     *
     * @param openApi
     *            The base object containing the global description through
     *            "Info" class
     * @return Void
     */
    private void preparHtmlForGlobalDescription(OpenAPI openApi) {
        String currentDescription = openApi.getInfo().getDescription();
        if (currentDescription != null && !currentDescription.isEmpty()) {
            CommonmarkMarkdownConverter markInstance = new CommonmarkMarkdownConverter();
            openApi.getInfo().setDescription(markInstance.toHtml(currentDescription));
        } else {
            LOGGER.error("Swagger object description is empty [" + openApi.getInfo().getTitle() + "]");
        }
    }

    private void prepareHtmlForPathDescriptions(final OpenAPI openApi) {

        final Stream<Operation> operationFromAllPaths = openApi.getPaths().values().stream()
            .flatMap(pathItem -> Stream.of(
                pathItem.getHead(),
                pathItem.getOptions(),
                pathItem.getGet(),
                pathItem.getTrace(),
                pathItem.getPost(),
                pathItem.getPut(),
                pathItem.getPatch(),
                pathItem.getDelete()
            ).filter(Objects::nonNull));

        operationFromAllPaths.forEach(operation -> {
            final String markdownDescription = operation.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            operation.setDescription(htmlDescription);
        });
    }

    private void prepareHtmlForPathParameters(final OpenAPI openApi) {

        final Stream<Parameter> parameterFromAllPaths = openApi.getPaths().values().stream()
            .map(PathItem::getParameters)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream);

        parameterFromAllPaths.forEach(parameter -> {
            final String markdownDescription = parameter.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            parameter.setDescription(htmlDescription);
        });
    }

    private void prepareHtmlForMethodsParameters(final OpenAPI openApi) {

        final Stream<Parameter> parameterFromAllMethodsOfAllPaths = openApi.getPaths().values().stream()
            .flatMap(pathItem -> Stream.of(
                pathItem.getHead(),
                pathItem.getOptions(),
                pathItem.getGet(),
                pathItem.getTrace(),
                pathItem.getPost(),
                pathItem.getPut(),
                pathItem.getPatch(),
                pathItem.getDelete()
            ).filter(Objects::nonNull))
            .flatMap(operation -> Optional.ofNullable(operation.getParameters()).orElse(emptyList()).stream())
            .filter(Objects::nonNull);

        parameterFromAllMethodsOfAllPaths.forEach(parameter -> {
            final String markdownDescription = parameter.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            parameter.setDescription(htmlDescription);
        });
    }

    @Override
    public void addHandlebarHelpers(final Handlebars handlebars) {
        super.addHandlebarHelpers(handlebars);
        handlebars.with(EscapingStrategy.NOOP);
    }
}