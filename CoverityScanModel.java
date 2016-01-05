
import com.coverity.primitives.Resource_LeakPrimitives;

/**
 * https://scan.coverity.com/models#java_models_annotations
 */
public class CoverityScanModel {

    public CoverityScanModel() {
        Resource_LeakPrimitives.open(this);
    }

    public void close() {
        Resource_LeakPrimitives.close(this);
    }
}
