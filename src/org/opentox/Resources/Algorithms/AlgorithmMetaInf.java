package org.opentox.Resources.Algorithms;

import java.util.ArrayList;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmMetaInf {
    
    private String title,
            description,
            subject,
            type,
            source,
            relation,
            rights,
            provenance,
            audience,
            algorithmType,
            identifier,
            date,
            format,
            language,
            creator,
            publisher,
            contributor,
            about
            ;

    private ArrayList<String> statisticsSupported;
    
    private String[][] Parameters;



    public AlgorithmMetaInf(){

    }


    public AlgorithmMetaInf(String about){
        this.about=about;
    }


    public void setAbout(String about) {
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

    /**
     * <b>Label:</b> Relation<br/><br/>
     * <b>Element Description:</b> A reference to a related resource.
     * Recommended best practice is to reference the resource by means of a
     * string or number conforming to a formal identification system.<br/><br/>
     * <b>Guidelines for content creation:</b>
     * Relationships may be expressed reciprocally (if the resources on both ends
     * of the relationship are being described) or in one direction only, even
     * when there is a refinement available to allow reciprocity. If text strings
     * are used instead of identifying numbers, the reference should be appropriately
     * specific. For instance, a formal bibliographic citation might be used to
     * point users to a particular resource.Because the refined terms used with
     * Relation provide significantly more information to a user than the unqualified
     * use of Relation, implementers who are describing heavily interrelated
     * resources might choose to use qualified Dublin Core.<br/><br/>
     * <b>Examples:</b>
     * <pre> Title="Reading Turgenev"
     * Relation="Two Lives" [Resource is a collection of two novellas,
     * one of which is "Reading Turgenev"]
     * [Relationship described is <b>IsPartOf.</b>]</pre>
     * [Part/Whole relations are those in which one resource is
     * a physical or logical part of another]
     * <pre> Title="Candle in the Wind"
     * Subject="Diana, Princess of Wales"
     * Date="1997"
     * Creator="John, Elton"
     * Type="sound"
     * Description="Tribute to a dead princess."
     * Relation="Elton John's 1976 song Candle in the Wind"</pre>
     * [Relationship described is IsVersionOf.]
     * @param relation
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * <b>Label:</b> Creator<br/><br/>
     * <b>Element Description:</b> An entity primarily responsible for making
     * the content of the resource. Examples of a Creator include a person,
     * an organization, or a service. Typically the name of the Creator should
     * be used to indicate the entity.<br/><br/>
     * <b>Guidelines for creation of content:</b>
     * Creators should be listed separately, preferably in the same order that
     * they appear in the publication. Personal names should be listed surname
     * or family name first, followed by forename or given name. When in doubt,
     * give the name as it appears, and do not invert. In the case of organizations
     * where there is clearly a hierarchy present, list the parts of the hierarchy
     * from largest to smallest, separated by full stops and a space. If it is
     * not clear whether there is a hierarchy present, or unclear which is the
     * larger or smaller portion of the body, give the name as it appears in the item.
     * If the Creator and Publisher are the same, do not repeat the name in the
     * Publisher area. If the nature of the responsibility is ambiguous, the
     * recommended practice is to use Publisher for organizations, and Creator
     * for individuals. In cases of lesser or ambiguous responsibility, other
     * than creation, use Contributor.<br/><br/>
     * <b>Examples:</b>
     * <pre> Creator="Shakespeare, William"
     * Creator="Wen Lee"
     * Creator="Hubble Telescope"
     * Creator="Internal Revenue Service. Customer Complaints Unit"</pre>
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * <b>Label:</b> Publisher<br/><br/>
     * <b>Element Description:</b> The entity responsible for making the resource
     * available. Examples of a Publisher include a person, an organization, or
     * a service. Typically, the name of a Publisher should be used to indicate
     * the entity.<br/><br/>
     * <b>Guidelines for content creation:</b>
     * The intent of specifying this field is to identify the entity that provides
     * access to the resource. If the Creator and Publisher are the same, do not
     * repeat the name in the Publisher area. If the nature of the responsibility
     * is ambiguous, the recommended practice is to use Publisher for organizations,
     * and Creator for individuals. In cases of ambiguous responsibility,
     * use Contributor.<br/><br/>
     * <b>Examples:</b>
     * <pre> Publisher="University of South Where"
     * Publisher="Funky Websites, Inc."
     * Publisher="Carmen Miranda"</pre>
     * @param publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * <b>Label:</b> Contributor<br/><br/>
     * <b>Element Description:</b> An entity responsible for making contributions
     * to the content of the resource. Examples of a Contributor include a person,
     * an organization or a service. Typically, the name of a Contributor should
     * be used to indicate the entity.<br/><br/>
     * <b>Guideline for content creation:</b>
     * The same general guidelines for using names of persons or organizations as
     * Creators apply here. Contributor is the most general of the elements used
     * for "agents" responsible for the resource, so should be used when primary
     * responsibility is unknown or irrelevant.
     * @param contributor
     */
    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    /**
     *
     * <b>Label:</b> Rights Management<br/><br/>
     * <b>Element Description:</b> Information about rights held in and over the
     * resource. Typically a Rights element will contain a rights management
     * statement for the resource, or reference a service providing such information.
     * Rights information often encompasses Intellectual Property Rights (IPR),
     * Copyright, and various Property Rights. If the rights element is absent,
     * no assumptions can be made about the status of these and other rights
     * with respect to the resource.<br/><br/>
     * <b>Guidelines for content creation:</b>
     * The Rights element may be used for either a textual statement or a URL
     * pointing to a rights statement, or a combination, when a brief statement
     * and a more lengthy one are available.<br/><br/>
     * <b>Examples:</b>
     * <pre> Rights="Access limited to members"
     * Rights="http://cs-tr.cs.cornell.edu/Dienst/
     * Repository/2.0/Terms"</pre>
     */
    public void setRights(String rights) {
        this.rights = rights;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * <b>Label:</b> Format<br/><br/>
     * <b>Element Description:</b> The physical or digital manifestation of the
     * resource. Typically, Format may include the media-type or dimensions of
     * the resource. Examples of dimensions include size and duration. Format
     * may be used to determine the software, hardware or other equipment needed
     * to display or operate the resource.
     * Recommended best practice is to select a value from a controlled vocabulary
     * (for example, the list of <b>Internet Media Types</b>
     * [http://www.iana.org/ assignments/media-types/] defining computer media formats).
     * <br/><br/>
     * <b>Guidelines for content creation:</b>
     * In addition to the specific physical or electronic media format, information
     * concerning the size of a resource may be included in the content of the
     * Format element if available. In resource discovery size, extent or medium
     * of the resource might be used as a criterion to select resources of
     * interest, since a user may need to evaluate whether they can make use of
     * the resource within the infrastructure available to them.
     * When more than one category of format information is included in a single
     * record, they should go in separate iterations of the element.
     * @param format
     */
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

    /**
     * <b>Label:</b> Provenance<br/><br/>
     * <b>Element Description:</b> A statement of any changes in ownership and
     * custody of the resource since its creation that are significant for its
     * authenticity, integrity and interpretation. The statement may include a
     * description of any changes successive custodians made to the resource.<br/>
     * <br/>
     * <b>Guidelines for content creation:</b><br/><br/>
     * <b>Examples:</b>
     * <pre> Provenance="This copy once owned by Benjamin Spock."
     * Provenance="Estate of Hunter Thompson."
     * Provenance="Stolen in 1999; recovered by the Museum in 2003."</pre>
     * @param provenance
     */
    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    /**
     * <b>Label:</b> Title<br/><br/>
     * <b>Element Description:</b> The name given to the resource.
     * Typically, a Title will be a name by which the resource is formally known.<br/><br/>
     * <b>Guidelines for creation of content:</b>
     * If in doubt about what constitutes the title, repeat the Title element and
     * include the variants in second and subsequent Title iterations.
     * If the item is in HTML, view the source document and make sure that the
     * title identified in the title header (if any) is also included as a Title.<br/><br/>
     * <b>Examples:</b>
     * <pre> Title="A Pilot's Guide to Aircraft Insurance"
     * Title="The Sound of Music"
     * Title="Green on Greens"
     * Title="AOPA's Tips on Buying Used Aircraft"</pre>
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <b>Label:</b> Resource Identifier<br/><br/>
     * <b>Element Description:</b> An unambiguous reference to the resource
     * within a given context. Recommended best practice is to identify the
     * resource by means of a string or number conforming to a formal
     * identification system. Examples of formal identification systems
     * include the Uniform Resource Identifier (URI) (including the
     * Uniform Resource Locator (URL), the Digital Object
     * Identifier (DOI) and the International Standard Book Number (ISBN).<br/><br/>
     * <b>Guidelines for content creation:</b>
     * This element can also be used for local identifiers (e.g. ID numbers
     * or call numbers) assigned by the Creator of the resource to apply to
     * a particular item. It should not be used for identification of the
     * metadata record itself.<br/><br/>
     * <b>Examples</b>
     * <pre> Identifier="http://purl.oclc.org/metadata/dublin_core/"
     * Identifier="ISBN:0385424728"
     * Identifier="H-A-X 5690B" [publisher number]</pre>
     * @param identifier The resource identifier
     * @see http://dublincore.org/documents/usageguide/elements.shtml
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Specify the algorithm type according to a formal ontology.
     * @param AlgorithmType
     */
    public void setAlgorithmType(String AlgorithmType) {
        this.algorithmType = AlgorithmType;
    }

    /**
     * Define the characteristics of the algorithm. The <em>ArrayList&lt;String&gt;
     * </em>element statisticsSupported is a list of the supported statistics by the
     * algorithm. The String array <em>Parameters</em> is the set of tuning parameters
     * of the algorithm, i.e. data that can be posted to the algorithm.<br/><br/>
     * <b>Note:</b> Parameters is a N-by-3 matrix of type String. The first
     * column contains the name of the parameter, the second the datatype and
     * the third the default value.
     * @param statisticsSupported
     * @param Parameters
     */
    public void setAlgorithm(ArrayList<String> statisticsSupported,
            String[][] Parameters) {
        this.statisticsSupported = statisticsSupported;
        this.Parameters = Parameters;
    }



    public String getAlgorithmType() {
        return algorithmType;
    }

    public String getAudience() {
        return audience;
    }

    public String getContributor() {
        return contributor;
    }

    public String getCreator() {
        return creator;
    }

    public String getDate() {
        return date;
    }

    public String getFormat() {
        return format;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLanguage() {
        return language;
    }

    public String[][] getParameters() {
        return Parameters;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getRelation() {
        return relation;
    }

    public String getRights() {
        return rights;
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getStatisticsSupported() {
        return statisticsSupported;
    }

    public String getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getAbout() {
        return about;
    }




}
