package org.edu.test;

public class Example {
public static void main(String[] args) {
	// C변수는 A,B를 상속받은 interface 
	// a,b 는 c를 상속하고있는 부모변수 
	
	ImplementationC impl = new ImplementationC();
	InterfaceA ia = impl;
	ia.methodA();
	System.out.println( "a 변수는 method a 만 호출가능");
	// A , B 는 부모변수라 자기 객체만 사용가능 
	// C 는 상속을받고있기떄문에 A,B  자기 자신객체 C 를 포함하여 메서드 호출가능
	InterfaceB ib = impl;
	ib.methodB();
	System.out.println("b 변수는 method b 만 호출가능");
	InterfaceC ic = impl;
	ic.methodA();
	ic.methodB();
	ic.methodC();
	System.out.println("C 변수는 a b c 다 호출 가능");
	}
}
