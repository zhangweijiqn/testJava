package zwj.test.callback;

/*业务处理类来处理逻辑，并在处理完毕之后，执行callback函数。
 * */

public class TestCallback {
	public void compute(int n, ComputeCallBack callback) {  //ComputeCallBack必须为一个接口，这里的回调方式类似于js中函数的回调函数参数
        //由于ComputeCallBack为接口，在传递该参数时必须实现接口中的方法
        for (int i = 0; i < n; i++) {  
            System.out.println(i);  
        }  
        callback.onComputeEnd();//这里调用了callback接口的函数  
    }  
}

