package zwj.test.callback;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestCallback().compute(1000, new ComputeCallBack() {//匿名内部类,第二个参数为一个接口,在传递该参数时必须实现接口中的方法
			@Override  
            public void onComputeEnd() {  
                System.out.println("end back!!!");
            }  
        });  
	}
}
