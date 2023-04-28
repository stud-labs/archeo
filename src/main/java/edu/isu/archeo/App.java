package edu.isu.archeo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ontoMonad();
    }
    public static String 
        url = "https://gist.githubusercontent.com/eugeneai" +
        "/141f89eace745682ece44a9fc4245085/raw/cb84c1892b642af894ac5a9d0ec19cc177552c31/" + 
        "foaf.rdf";
    public static String
        rdfFile = "./foaf.rdf";
    public static String
        rdfOutFile = "./foaf-out.rdf";
    public static void ontoMonad () {
        System.out.println( "Hello from a Monad" );
        System.out.println( url );
        System.out.println( "Hello from a Monad" );        
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        OntDocumentManager manager = model.getDocumentManager();
        manager.setProcessImports(true);
        // manager.addIgnoreImport(...);
        // manager.addAltEntry(url, url);
        // model.read(url); // original download
        // BufferedReader rd = new BufferedReader(new FileReader(rdfFile));
        model.read(rdfFile);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rdfOutFile));
            // model.write(System.out);
            model.write(writer);
            
            for (ExtendedIterator iter = model.listIndividuals(); iter.hasNext();) { 
                OntClass tempClass = (OntClass) iter.next();
                System.out.println(tempClass.getLocalName());
            }


            System.out.println( "Signing OK!" );
        } catch (IOException exc) {
            System.out.println( "Writing problems!" );
        }
    }
}
