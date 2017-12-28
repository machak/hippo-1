package uk.nhs.digital.ps.test.acceptance.data;

import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.models.PublicationSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static uk.nhs.digital.ps.test.acceptance.data.TestDataRepo.PublicationClassifier.SINGLE;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

public class TestDataRepo {

    private Map<PublicationClassifier, List<Publication>> publications = new HashMap<>();

    private List<Attachment> attachments;
    private PublicationSeries publicationSeries;
    private String datasetName;

    public void setPublication(final Publication publication) {
        publications.clear();
        addPublications(SINGLE, publication);
    }

    public Publication getCurrentPublication() {
        List<Publication> publications = this.publications.get(SINGLE);
        return publications == null ? null : publications.get(0);
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setPublicationSeries(final PublicationSeries publicationSeries) {
        this.publicationSeries = publicationSeries;
    }

    public PublicationSeries getPublicationSeries() {
        return publicationSeries;
    }

    public void addPublications(final PublicationClassifier publicationClassifier, final Publication... publications) {
        addPublications(publicationClassifier, asList(publications));
    }

    public void addPublications(final PublicationClassifier publicationClassifier, final List<Publication> publications) {
        if (!this.publications.containsKey(publicationClassifier)) {
            this.publications.put(publicationClassifier, new ArrayList<>());
        }
        this.publications.get(publicationClassifier).addAll(publications);
    }

    public List<Publication> getPublications(final PublicationClassifier publicationClassifier) {
        return publications.get(publicationClassifier);
    }

    public void clear() {
        setPublication(null);
        publications.clear();
        datasetName = null;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String createDatasetName() {
        datasetName = newRandomString() + "dataset";
        return datasetName;
    }

    public enum PublicationClassifier {
        SINGLE,
        UPCOMING,
        LIVE;
    }
}
