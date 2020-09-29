package org.edu.test;

public class ImplementationC  implements InterfaceC{
// 메서드 a b 는 부모의 인터페이스를 C 인 자식이 사용할수있음 
	// a.b 는 각각의 본인의 메서드만 사용가능 
	  // 결과적으로 
	@Override
	public void methodA() {
		// TODO Auto-generated method stub
		System.out.println(" A실행");
	}

	@Override
	public void methodB() {
		// TODO Auto-generated method stub
		System.out.println(" B실행");
	}

	@Override
	public void methodC() {
		// TODO Auto-generated method stub
		System.out.println(" C실행");
	}

}
