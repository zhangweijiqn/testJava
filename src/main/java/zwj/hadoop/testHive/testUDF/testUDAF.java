package zwj.hadoop.testHive.testUDF;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFVariance;
import org.apache.hadoop.hive.ql.util.JavaDataModel;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

/**
 * Created by zhangwj on 16-3-8.
 * UDAF代码参考hive源码
 * UDAF已经被deprecated，推荐使用的有两种方式：
 *          Either implement org.apache.hadoop.hive.ql.udf.generic.GenericUDAFResolver2
 *          or extend org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver instead.
 *
 * 类AbstractGenericUDAFResolver是实现的GenericUDAFResolver2
 */
public class testUDAF extends AbstractGenericUDAFResolver {
    static final Log logger = LogFactory.getLog(testUDAF.class);

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
        if (parameters.length != 1) {
            throw new UDFArgumentTypeException(parameters.length - 1,
                    "Exactly one argument is expected.");
        }

        if (parameters[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentTypeException(0,
                    "Only primitive type arguments are accepted but "
                            + parameters[0].getTypeName() + " is passed.");
        }
        switch (((PrimitiveTypeInfo) parameters[0]).getPrimitiveCategory()) {
            case BYTE:
            case SHORT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
            case TIMESTAMP:
            case DECIMAL:
                return new GenericUDAFStdSampleEvaluator();
            case BOOLEAN:
            case DATE:
            default:
                throw new UDFArgumentTypeException(0,
                        "Only numeric or string type arguments are accepted but "
                                + parameters[0].getTypeName() + " is passed.");
        }
    }

    /**
     * Compute the sample standard deviation by extending
     * GenericUDAFVarianceEvaluator and overriding the terminate() method of the
     * evaluator.
     */
    public static class GenericUDAFStdSampleEvaluator extends
            GenericUDAFVariance.GenericUDAFVarianceEvaluator {

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            StdAgg myagg = (StdAgg) agg;

            if (myagg.count == 0) { // SQL standard - return null for zero elements
                return null;
            } else {
                if (myagg.count > 1) {
                    getResult().set(Math.sqrt(myagg.variance / (myagg.count - 1)));
                } else { // for one element the variance is always 0
                    getResult().set(0);
                }
                return getResult();
            }
        }
    }

    @GenericUDAFEvaluator.AggregationType(estimable = true)
    static class StdAgg extends GenericUDAFEvaluator.AbstractAggregationBuffer {
        long count; // number of elements
        double sum; // sum of elements
        double variance; // sum[x-avg^2] (this is actually n times the variance)
        @Override
        public int estimate() { return JavaDataModel.PRIMITIVES2 * 3; }
    };

}
