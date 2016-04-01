import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by HörðurMár on 31.3.2016.
 */
public class ConceptNet {

    /*
     * Returns a valid ConceptNet URI for a given text, where the text
     * is from the english language.
     */
    public static String getValidURI(String concept) throws IOException {

        String s = "http://conceptnet5.media.mit.edu/data/5.4/uri?language=en&text=" + concept;
        URL url = new URL(s);

        /*
         * Read the URL
         */
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        return str;
    }

    public static Edge similarityScore(String concept, String conceptCmp) throws IOException, JSONException {

        String s = "http://conceptnet5.media.mit.edu/data/5.4/assoc/c/en/" + concept + "?filter=/c/en/" + conceptCmp + "/.&limit=1";
        URL url = new URL(s);

        /*
         * Read the URL
         */
        Scanner scan = new Scanner(url.openStream());
        String str = new String();
        while (scan.hasNext())
            str += scan.nextLine();
        scan.close();

        //
        JSONObject obj = new JSONObject(str);
        JSONArray array = obj.getJSONArray("similar");
        String con = (String)array.getJSONArray(0).get(0);
        double score = (double)array.getJSONArray(0).get(1);
        Edge edge = new Edge(con,score);

        return edge;
    }

    public static Collection<Edge> test(String concept, ArrayList<String> list) throws IOException, JSONException {

        Collection<Edge> coll = new ArrayList<>();
        Edge edge = similarityScore(concept,concept);
        coll.add(edge);

        for (int i = 0; i < list.size(); i++) {
            Edge currEdge = similarityScore(concept, list.get(i));
            coll.add(currEdge);
        }

        return coll;
    }
}
