package edu.isu.archeo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecException;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static String 
        url = "https://gist.githubusercontent.com/eugeneai" +
        "/141f89eace745682ece44a9fc4245085/raw/cb84c1892b642af894ac5a9d0ec19cc177552c31/" + 
        "foaf.rdf";
    public static String
        // rdfFile = "./foaf.rdf";
        rdfFile = "./geolprocessesowl7.rdf";
    public static String
        rdfOutFile = "./foaf-out.rdf";
    public static void ontoMonad () {
        // Laboratory work No 3. A part.
        System.out.println( url );
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        OntDocumentManager manager = model.getDocumentManager();
        manager.setProcessImports(true);
        // manager.addIgnoreImport(...);
        // manager.addAltEntry(url, url);
        // model.read(url); // original download
        // BufferedReader rd = new BufferedReader(new FileReader(rdfFile));
        model.read(rdfFile);
        
        for (ExtendedIterator iter = model.listClasses(); iter.hasNext();) { 
            OntClass tempClass = (OntClass) iter.next();
            System.out.println(tempClass.getLocalName());
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rdfOutFile));
            // model.write(System.out);
            model.write(writer);
            // for(start; loop condition; end body op)

            System.out.println( "Writing OK!" );
        } catch (IOException exc) {
            System.out.println( "Writing problems!" );
        }
    }

    public static void query (OntModel model, String q) {
        // Laboratory work No 4.
        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.create(query,model);
        System.out.println("-----------------------------------------------");
        System.out.println(q);
        try {
            if (q.contains("CONSTRUCT")) {
                Model res = qexec.execConstruct();
                res.write(System.out, "TURTLE");
                model.add(res);
            } else {
                ResultSet results = qexec.execSelect();
                ResultSetFormatter.out (results, query);
            }
        } catch (QueryExecException e){
            System.out.println("No data returned!");
        } finally {
            qexec.close();
        }
    }

    public static void queries () {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(rdfFile);
        
        String pref = "PREFIX g: <http://www.semanticweb.org/moks/ontologies/2023/1/geolprocesses#>";
        String q1 = String.join("\n",
        pref
        , "SELECT ?s"
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "}");
        
        String q2 = String.join("\n",
        pref
        , "SELECT ?s"
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "    ?s g:протяженность ?l ."
        , "    FILTER ( ?l > 300.0)." 
        , "}"
        );

        String q3 = String.join("\n",
        pref
        , "SELECT ?s"
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "    ?s g:протяженность ?l ."
        , "    FILTER ( ?l < 2000.0)." 
        , "}"
        );

        String q4 = String.join("\n",
        pref
        , "CONSTRUCT {"
        , "?s g:крутизна_склона g:пологий "
        , "} "
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "    ?s g:крутизна ?k ."
        , "    FILTER ( ?k <= 2.0)." 
        , "}"
        );

        String q4test = String.join("\n",
        pref
        , "SELECT ?s"
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "    ?s g:крутизна_склона g:пологий ."
        , "}"
        );

        String q4test1 = String.join("\n",
        pref
        , "SELECT ?s"
        , "WHERE {"
        , "    ?s a g:Участок ."
        , "    ?s g:крутизна ?k ."
        , "    FILTER ( ?k <= 2.0)." 
        , "}"
        );

        ArrayList<String> l = new ArrayList<String>();
        l.add(q1); l.add(q2); l.add(q3); 
        l.add(q4); l.add(q4test); l.add(q4test1);

        for (String q: l) {
            query(model, q);
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("geoprocess-out.ttl"));
            // model.write(System.out);
            model.write(writer,"TURTLE");
            // for(start; loop condition; end body op)

            System.out.println( "Writing OK!" );
        } catch (IOException exc) {
            System.out.println( "Writing problems!" );
        }    
    }
    public static void main( String[] args )
    {
        System.out.println( "Jena based Geoprocess query tool!" );
        ontoMonad();
        System.out.println( "------- Try SPARQL queries -----" );
        queries();
    }
}
