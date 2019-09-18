package ca.corefacility.bioinformatics.irida.service.impl;

import ca.corefacility.bioinformatics.irida.model.sample.metadata.MetadataEntry;
import ca.corefacility.bioinformatics.irida.model.sample.metadata.OntologyMetadataEntry;
import com.google.common.collect.Lists;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MetadataEntryOntologyServiceImpl {

	private static final List<String> GENEPIO_FILES = Lists.newArrayList(
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/apollo_sv_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/bccdc_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/bfo.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/brenda_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/chebi_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/chmo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/doid_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/efo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/envo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/eo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ero_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/evs_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/fix_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/foodon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/gazetteer_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/gaz_insdc_mapping.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/genepio.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/geo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/hardcoded.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/hp_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ido_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/irida_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/mapping_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/model_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ncbitaxon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ncit_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ndf-rt_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/oae_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/obi_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/omit_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ontology-metadata.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/pato_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/pco_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/po_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/public_health_lab_epi_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/ro_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/sio_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/so_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/stato_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/typon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/uberon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/genepio/uo_import.owl");

	private static List<String> FOODON_FILES = Lists.newArrayList(
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/agency_categories.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ancestro_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ancestro_import.owl.bak.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/bfo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/bfo.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/chebi_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ecocore_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ecocore.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/efo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/envo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/eo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/eupath_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/foodon.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/foodon_product_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/food_product_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/gaz_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/geem_import2.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/geem_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/hancestro_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/hp_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/langual_deprecated_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/langual_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/metadata_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ncbitaxon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ncit_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/obi_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/pato_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/peco_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/po_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/product_type_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/ro_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/siren_augment_codes.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/siren_augment.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/subset_siren_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/uberon_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/uo_import.owl",
			"/ca/corefacility/bioinformatics/irida/ontology/foodon/wine_pasta.owl");

	private Model model;
	private Dataset dataset;

	public MetadataEntryOntologyServiceImpl() {
		dataset = DatasetFactory.create();
		//dataset = TDB2Factory.connectDataset("/tmp/jena");//DatasetFactory.createMem();//TDBFactory.createDataset("/tmp/jena");

		model = dataset.getDefaultModel();

		try {
			dataset.begin(ReadWrite.WRITE);
			GENEPIO_FILES.stream()
					.forEach(f -> {
						ClassPathResource resource = new ClassPathResource(f);
						Path path = Paths.get(resource.getPath());
						RDFDataMgr.read(model, path.toString());
					});

			FOODON_FILES.stream()
					.forEach(f -> {
						try {
							ClassPathResource resource = new ClassPathResource(f);
							Path path = Paths.get(resource.getPath());
							RDFDataMgr.read(model, path.toString());
						} catch (Exception e) {
							System.out.println("didn't load " + f + " because of " + e.getMessage());
						}
					});
			dataset.commit();
		} catch (Exception e) {
			System.out.println("bad txn " + e.getMessage());
		}
	}

	public List<MetadataEntry> getOntologyTerm(String term, String datatype) {
		switch (datatype) {
		case "symptom":
			return getSymptom(term);
		case "building":
			return getBuilding(term);
		case "bodypart":
			return getBodypart(term);
		case "food":
			return getFood(term);
		}

		return null;
	}

	public List<MetadataEntry> getSymptom(String symptom) {
		// @formatter:off
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n "
				+" SELECT DISTINCT ?subject ?search\n"
				+ "WHERE { ?pred rdfs:subPropertyOf obo:IAO_0000118.\n"
				+ "  {?subject ?pred ?search} UNION {?subject rdfs:label ?search}.\n"
				+ "  ?subject rdfs:subClassOf* obo:HP_0000118\n"
				+ "  FILTER regex(?search, ?term, 'i').}\n";
		// @formatter:on

		dataset.begin(ReadWrite.READ);
		ParameterizedSparqlString query = new ParameterizedSparqlString(queryString);

		query.setLiteral("term", QueryParser.escape(symptom));
		Query q = query.asQuery();
		QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
		ResultSet result = qexec.execSelect();

		List<MetadataEntry> results = collectResults(result, "subject", "search", "symptom");

		return results;
	}

	public List<MetadataEntry> getBuilding(String symptom) {
		// @formatter:off
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n "
				+" SELECT DISTINCT ?subject ?search\n"
				+ "WHERE { ?pred rdfs:subPropertyOf obo:IAO_0000118.\n"
				+ "  {?subject ?pred ?search} UNION {?subject rdfs:label ?search}.\n"
				+ "  ?subject rdfs:subClassOf* <http://purl.obolibrary.org/obo/ENVO_00000073>\n"
				+ "  FILTER regex(?search, ?term, 'i').}\n";
		// @formatter:on

		dataset.begin(ReadWrite.READ);
		ParameterizedSparqlString query = new ParameterizedSparqlString(queryString);

		query.setLiteral("term", QueryParser.escape(symptom));
		Query q = query.asQuery();
		QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
		ResultSet result = qexec.execSelect();

		List<MetadataEntry> results = collectResults(result, "subject", "search", "building");

		return results;
	}

	public List<MetadataEntry> getBodypart(String symptom) {
		// @formatter:off
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n "
				+" SELECT DISTINCT ?subject ?search\n"
				+ "WHERE { ?pred rdfs:subPropertyOf obo:IAO_0000118.\n"
				+ "  {?subject ?pred ?search} UNION {?subject rdfs:label ?search}.\n"
				+ "  ?subject rdfs:subClassOf* <http://purl.obolibrary.org/obo/UBERON_0000061>\n"
				+ "  FILTER regex(?search, ?term, 'i').}\n";
		// @formatter:on

		dataset.begin(ReadWrite.READ);
		ParameterizedSparqlString query = new ParameterizedSparqlString(queryString);

		query.setLiteral("term", QueryParser.escape(symptom));
		Query q = query.asQuery();
		QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
		ResultSet result = qexec.execSelect();

		List<MetadataEntry> results = collectResults(result, "subject", "search", "bodypart");

		return results;
	}

	public List<MetadataEntry> getFood(String symptom) {
		// @formatter:off
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
			+ "PREFIX obo: <http://purl.obolibrary.org/obo/>\n"
			+ "PREFIX food: <http://purl.org/foodontology#>\n"
			+ "\n"
			+ "SELECT DISTINCT ?subject ?search\n"
			+ "WHERE {\n"
			+ "  ?subject ?pred ?search.\n"
			+ "  ?subject rdfs:subClassOf* obo:FOODON_03400361.\n"
			+ "  ?subject rdfs:label ?search.\n"
			+ "  FILTER regex(?search, ?term, 'i').}\n";
		// @formatter:on

		dataset.begin(ReadWrite.READ);
		ParameterizedSparqlString query = new ParameterizedSparqlString(queryString);

		query.setLiteral("term", QueryParser.escape(symptom));
		Query q = query.asQuery();
		QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
		ResultSet result = qexec.execSelect();

		List<MetadataEntry> results = collectResults(result, "subject", "search", "food");

		return results;
	}

	private List<MetadataEntry> collectResults(ResultSet result, String subjectName, String literalName,
			String datatype) {
		List<MetadataEntry> results = new ArrayList<>();
		while (result.hasNext()) {
			QuerySolution next = result.next();
			String search = next.getLiteral(literalName)
					.getString();
			Resource resource = next.getResource(subjectName);
			String resourceURI = resource.getURI();

			results.add(new OntologyMetadataEntry(search, datatype, resourceURI));
		}
		dataset.end();

		return results;
	}

}
