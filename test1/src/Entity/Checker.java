package Entity;
interface Checker<A,B,R>{
	R check(A contents,B user);
}