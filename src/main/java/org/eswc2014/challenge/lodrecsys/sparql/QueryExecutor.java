package org.eswc2014.challenge.lodrecsys.sparql;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class QueryExecutor {

	private String uri;
	private String endpoint="http://dbpedia.org/sparql";
	private String graphURI=null;

	public void exec(String uri) {
		this.uri=uri;
		Query query;
		String q;

		String uriQuery = "<" + uri + ">";

		q = " SELECT * WHERE {{" + " ?s ?p " + uriQuery + ".   " +
				"FILTER isIRI(?s). " +
				" } UNION {"
				+ uriQuery + " ?p ?o " +
				"FILTER isIRI(?o). " +
				"}}";
		try {
			query = QueryFactory.create(q);

			execQuery(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void execQuery(Query query) {

		System.out.println("executing query  : " + query.toString());

		QueryExecution qexec = null;
		try {
			if (graphURI==null)
				qexec = QueryExecutionFactory.sparqlService(endpoint, query); 
			else
				qexec = QueryExecutionFactory.sparqlService(endpoint, query,graphURI);
			
				
			ResultSet results = qexec.execSelect();

			QuerySolution qs;
			RDFNode node, prop;
			
			String n,p;
			
			System.out.println("Results:");

			while (results.hasNext()) {

				qs = results.next();

				prop=qs.get("p");
				p=prop.toString();
				p = p.replace("<", "");
				p = p.replace(">", "");
			
				if (qs.get("o") == null) {
					node = qs.get("s");
					n = node.toString();
					n = n.replace("<", "");
					n = n.replace(">", "");
					
					System.out.println(n+'\t'+prop+'\t'+uri);
				} else {

					node = qs.get("o");
					n = node.toString();
					n = n.replace("<", "");
					n = n.replace(">", "");
					
					System.out.println(n+'\t'+prop+'\t'+uri);
					
				}

			}

		} finally {
			qexec.close();
		}

	}

	
	public static void main(String []args){
		
		
		QueryExecutor exec=new QueryExecutor();
		exec.exec("http://dbpedia.org/resource/The_Way_to_Dusty_Death");
		
	}
}
