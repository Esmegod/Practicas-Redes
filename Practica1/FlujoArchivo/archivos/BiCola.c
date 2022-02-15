   //Ruleta
   //Cola de prioridades

/*
Sigantura, parte semantica
Espec Bicola 
nuevaB:-> Bicola
formarBi:(Elem e, Bicola bc) -> Bicola 
formarBd:(Bicola bc. Elem e) -> Bicola
esnuevaB:(Bicola bc) -> Bool
izquierdoB:(Bicola bc) -> Elem
derechoB:(Bicola bc) -> Elem 
deformarBi:(Bicola bc)-> BiCola
deformarBd:(Bicola bc)-> BiCola

*/

/*
Axiomas: Elem e1, e2, Bicola q1;
 [b1] esnuevaB(nuevaB())=true;
 [b2] 
 
 [b7]derechoB(nuevaB())= ERROR;
 [b8]derechoB(formarBi(e1,q1)) = if(esnuevaB(q1)) return e1 else  return derechoB(q1)
 [b9]
 
 [b10]desformarBi
 [b11]desformarBi(formarBi(e1,q1)) = q1
 [b12]desformarBi(formarBd(q1, e1)) if(esnuevaB(q1)) return q1 else  return formarBd(desformarBi(), e1)
 
 [b13]desformarBd(nuevaB()) = ERROR
 [b14]desformarBd(formarBi(e1, q1)) if(esnuevaB(q1)) return q1 else  return formarBi(e1,desformarBd())
 [b15]desformarBd(formarBd(q1,e1)) = q1
*/

 typedef struct Nodo{
    Elem dato;
    struct Nodo *Der;
    struct Nodo *Izq;
  }*ApNodo ; 
  
  typedef struct cnodo{
  	ApNodo prim;
  	ApNodo ult;
  }*BiCola;
  
  
    BiCola nueva(){
  	BiCola t=(BiCola)malloc(sizeof(struct cnodo));
  	t->prim = NULL;
  	t->ult =NULL;
  	return t;
  }
  
    int esnuevaB(BiCola q){
  	return (q->prim==NULL);

  }
  
  
  
    
  Cola formarBi(Elem e, BiCola q){
  	ApNodo t = (ApNodo)malloc(sizeof(struct Nodo));
  	t->dato=e;
  	t->Der=NULL; 
  	t->Izq=NULL; 
  	
  	
	if(esnuevaB(q)){
		q->prim=q->ult=t;
	}
	else{
		t->Der = q->prim;
		q->prim->Izq = t;
		q->prim = t;
	
	}
	return q;
  }
  
  
  
    Cola formarBd(Elem e, BiCola q){
  	ApNodo t = (ApNodo)malloc(sizeof(struct Nodo));
  	t->dato=e;
  	t->Der=NULL; 
  	t->Izq=NULL; 
  	
  	
	if(esnuevaB(q)){
		q->prim=q->ult=t;
	}
	else{
		t->Izq = q->ult;
		q->ult->Der = t;
		q->ult= t;
	
	}
	return q;
  }
  
  
    Cola desformarBi(Cola q){
  	ApNodo t= q->prim;
  	if(q->prim==q->ult){
  		q->prim=q->ult= NULL;
	  }
	else 
	q->prim = t->Der;
	q->prim->Izq = NULL;
	free(t);
	return q;
  }
  
  
  
 Cola desformarBd(Cola q){
  	ApNodo t= q->ult;
  	if(q->prim==q->ult){
  		q->prim=q->ult= NULL;
	  }
	else 
	q->ult = t->Izq;
	q->ult->Der = NULL;
	free(t);
	return q;
  }
  
  
    Elem izquierdoB(BiCola q){
  	return q->prim->dato;
  }
  
      Elem derechoB(BiCola q){
  	return q->ult->dato;
  }
  
  
 
  
  
  
  
  
  
  
