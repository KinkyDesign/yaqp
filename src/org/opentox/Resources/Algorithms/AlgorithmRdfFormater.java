package org.opentox.Resources.Algorithms;


import org.opentox.Resources.AbstractResource;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 * Build an RDF representation for an Algorithm or a Model.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmRdfFormater extends AbstractAlgorithmFormater{

    private static final long serialVersionUID = 52795861750765264L;

    private String
            description,
            subject,
            type,
            source,
            relation,
            creator,
            publisher,
            contributor,
            rights,
            date,
            format,
            language,
            audience,
            provenance,
            about
            ;
    

    public AlgorithmRdfFormater(String about) {
        this.about = about;
    }

    

    /**
     * <b>Label:</b> Description<br/><br/>
     * <b>Element Description:</b> An account of the content of the resource.
     * Description may include but is not limited to: an abstract, table of contents,
     * reference to a graphical representation of content or a free-text account of the content.<br/><br/>
     * <b>Guidelines for creation of content:</b>
     * Since the Description field is a potentially rich source of indexable terms,
     * care should be taken to provide this element when possible.
     * Best practice recommendation for this element is to use full sentences,
     * as description is often used to present information to users to assist
     * in their selection of appropriate resources from a set of search results.
     * Descriptive information can be copied or automatically extracted from
     * the item if there is no abstract or other structured description available.
     * Although the source of the description may be a web page or other structured
     * text with presentation tags, it is generally not good practice to include HTML
     * or other structural tags within the Description element. Applications vary considerably
     * in their ability to interpret such tags, and their inclusion may negatively affect
     * the interoperability of the metadata.<br/><br/>
     * <b>Examples:</b>
     * <pre> Description="Illustrated guide to airport markings and lighting signals,
     *              with particular reference to SMGCS (Surface Movement Guidance
     *              and Control System) for airports with low visibility conditions."
     *
     * Description="Teachers Domain is a multimedia library for K-12 science educators,
     *              developed by WGBH through funding from the National Science Foundation
     *              as part of its National Science Digital Library initiative. The site
     *              offers a wealth of classroom-ready instructional resources, as well
     *              as online professional development materials and a set of tools
     *              which allows teachers to manage, annotate, and share the materials
     *              they use in classroom teaching."</pre>
     * @param description
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <b>Label:</b> Subject and Keywords<br/><br/>
     * <b>Element Description:</b> The topic of the content of the resource.
     * Typically, a Subject will be expressed as keywords or key phrases or
     * classification codes that describe the topic of the resource.
     * Recommended best practice is to select a value from a controlled vocabulary
     * or formal classification scheme.<br/><br/>
     * <b>Guidelines for creation of content:</b>
     * Select subject keywords from the Title or Description information,
     * or from within a text resource. If the subject of the item is a person
     * or an organization, use the same form of the name as you would if the person or
     * organization were a Creator or Contributor.
     * In general, choose the most significant and unique words for
     * keywords, avoiding those too general to describe a particular item.
     * Subject might include classification data if it is available
     * (for example, Library of Congress Classification Numbers or
     * Dewey Decimal numbers) or controlled vocabularies (such as Medical
     * Subject Headings or Art and Architecture Thesaurus descriptors) as well as keywords.
     * When including terms from multiple vocabularies, use separate element
     * iterations. If multiple vocabulary terms or keywords are used, either
     * separate terms with semi-colons or use separate iterations of the Subject element.<br/><br/>
     * <b>Examples:</b>
     * <pre> Subject="Aircraft leasing and renting"
     * Subject="Dogs"
     * Subject="Olympic skiing"
     * Subject="Street, Picabo"</pre>
     * @param subject
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * <b>Label:</b> Resource Type<br/><br/>
     * <b>Element Description:</b> The nature or genre of the content of the resource.
     * Type includes terms describing general categories, functions, genres, or aggregation
     * levels for content. Recommended best practice is to select a value from
     * a controlled vocabulary (for example, the DCMIType vocabulary ).
     * To describe the physical or digital manifestation of the resource,
     * use the FORMAT element.<br/><br/>
     * <b>Guidelines for content creation:</b>
     * If the resource is composed of multiple mixed types then multiple or
     * repeated Type elements should be used to describe the main components.
     * Because different communities or domains are expected to use a variety
     * of type vocabularies, best practice to ensure interoperability is to
     * include at least one general type term from the DCMIType vocabulary
     * in addition to the domain specific type term(s), in separate Type element iterations.
     * <br/><br/>
     * <b>Examples:</b>
     * <pre> Type="Image"
     * Type="Sound"
     * Type="Text"
     * Type="simulation"</pre>
     * @param type
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <b>Label:</b> Source<br/><br/>
     * <b>Element Description:</b> A Reference to a resource from which the
     * present resource is derived. The present resource may be derived from
     * the Source resource in whole or part. Recommended best practice is to
     * reference the resource by means of a string or number conforming to a
     * formal identification system.
     * <b>Guidelines for content creation:</b>
     * In general, include in this area information about a resource that is
     * related intellectually to the described resource but does not fit
     * easily into a Relation element.<br/><br/>
     * <b>Examples:</b>
     * <pre> Source="RC607.A26W574 1996" [where "RC607.A26W574 1996"
     *         is the call number of the print version of the resource,
     *         from which the present version was scanned]
     *
     * Source="Image from page 54 of the 1922 edition of Romeo
     *         and Juliet"</pre>
     * @param source
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setSource(String source) {
        this.source = source;
    }



    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    

    /**
     * <b>Label:</b> Language<br/><br/>
     * <b>Element Description:</b> A language of the intellectual content
     * of the resource. Recommended best practice for the values of the Language
     * element is defined by RFC 3066 [RFC 3066, http://www.ietf.org/rfc/
     * rfc3066.txt] which, in conjunction with ISO 639
     * [ISO 639, http://www.oasis- open.org/cover/iso639a.html]),
     * defines two- and three-letter primary language tags with optional subtags.
     * Examples include "en" or "eng" for English, "akk" for Akkadian, and
     * "en-GB" for English used in the United Kingdom.<br/><br/>
     * <b>Guidelines for content creation:</b>
     * Either a coded value or text string can be represented here.
     * If the content is in more than one language, the element may be repeated.<br/><br/>
     * <b>Examples:</b>
     * <pre> Language="en"
     * Language="fr"
     * Language="Primarily English, with some abstracts also in French."
     * Language="en-US"</pre><br/>
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * <b>Label:</b> Audience<br/><br/>
     * <b>Element Description:</b> A class of entity for whom the resource
     * is intended or useful. A class of entity may be determined by the
     * creator or the publisher or by a third party.<br/><br/>
     * <b>Guidelines for content creation:</b> Audience terms are best utilized
     * in the context of formal or informal controlled vocabularies.
     * None are presently recommended or registered by DCMI, but several
     * communities of interest are engaged in setting up audience vocabularies.
     * In the absence of recommended controlled vocabularies, implementors
     * are encouraged to develop local lists of values, and to use them consistently.
     * <br/><br/>
     * <b>Examples:</b>
     * <pre> Audience="elementary school students"
     * Audience="ESL teachers"
     * Audience="deaf adults"</pre><br/>
     * <b>NOTE:</b> Audience, Provenance and RightsHolder are elements,
     * but not part of the Simple Dublin Core fifteen elements.
     * Use Audience, Provenance and RightsHolder only when using Qualified Dublin Core.
     * <br/><br/>
     *
     * @param audience
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    /**
     * Redefine the RDF namespace which by default is set to
     * http://www.w3.org/1999/02/22-rdf-syntax-ns#
     * @param rdfns A new namespace for RDF
     */
    public void setRdfNamespace(String rdfns) {
        this.rdfNamespace = rdfns;
    }

    /**
     * Update the namespace for OpenTox Models and Algorithms.
     * @param otns
     */
    public void setOpentoxNamespace(String otns) {
        this.opentoxNamespace = otns;
    }

    /**
     * Update the Dublin Core namespace.
     * @param dcns new DCNS
     */
    public void setDcNamespace(String dcns) {
        this.dcNamespace = dcns;
    }

    

    private String opentoxAlgoritmElement() {
        StringBuilder builder = new StringBuilder();
        builder.append("<ot:algorithm name=\"" + title + "\" ot:id=\"" + identifier + "\">\n");
        builder.append("<ot:algorithmType>" + algorithmType + "</ot:algorithmType>\n");

        builder.append("<ot:Parameters>\n");
        if (Parameters[0].length != 3) {
            System.err.println("ERROR!!! Invalid Parameters Element!");
        } else {
            for (int i = 0; i < Parameters.length; i++) {
                builder.append("<ot:param ot:type=\"" + Parameters[i][1] + "\" " +
                        "ot:defaultvalue=\"" + Parameters[i][2] + "\">" +
                        Parameters[i][0] + "</ot:param>\n");
            }
        }
        builder.append("</ot:Parameters>\n");

        if (statisticsSupported.isEmpty()) {
            builder.append("<ot:statisticsSupported/>\n");
        } else {
            builder.append("<ot:statisticsSupported>\n");
            for (int i = 0; i < statisticsSupported.size(); i++) {
                builder.append("<ot:statistic>" + statisticsSupported.get(i) + "</ot:statistic>\n");
            }
            builder.append("</ot:statisticsSupported>\n");
        }
        builder.append("</ot:algorithm>\n");
        return builder.toString();
    }


    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(AbstractResource.xmlIntro);

        builder.append("<rdf:RDF " +
                "xmlns:rdf=\"" + rdfNamespace + "\" " +
                "xmlns:dc=\"" + dcNamespace + "\" " +
                "xmlns:ot=\"" + opentoxNamespace + "\">\n");

        builder.append("<rdf:Description rdf:about=\"" + about + "\">\n");
        builder.append("<dc:title>" + title + "</dc:title>\n");
        builder.append("<dc:subject>" + subject + "</dc:subject>\n");
        builder.append("<dc:description>" + description + "</dc:description>\n");
        builder.append("<dc:type>" + type + "</dc:type>\n");
        builder.append("<dc:source>" + source + "</dc:source>\n");
        builder.append("<dc:relation>" + relation + "</dc:relation>\n");
        builder.append("<dc:rights>" + rights + "</dc:rights>\n");
        builder.append("<dc:creator>" + creator + "</dc:creator>\n");
        builder.append("<dc:publisher>" + publisher + "</dc:publisher>\n");
        builder.append("<dc:contributor>" + contributor + "</dc:contributor>\n");
        builder.append("<dc:date>" + date + "</dc:date>\n");
        builder.append("<dc:format>" + format + "</dc:format>\n");
        builder.append("<dc:identifier>" + identifier + "</dc:identifier>\n");
        builder.append("<dc:audience>" + audience + "</dc:audience>\n");
        builder.append("<dc:provenance>" + provenance + "</dc:provenance>\n");
        builder.append("<dc:language>" + language + "</dc:language>\n");
        builder.append(opentoxAlgoritmElement());
        builder.append("</rdf:Description>\n");
        builder.append("</rdf:RDF>\n\n");
        return new StringRepresentation(builder.toString(),MediaType.APPLICATION_RDF_XML);
    }



}
