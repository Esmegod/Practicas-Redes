/*	
	Esmeralda Godinez Montero 
	D. Life of a flower
	18/02/2022 
*/

#include<iostream>
using namespace std;

int main(){
	int casos, dias, altura, actual, anterior=-1;
	cin>>casos;
	for(int i=0; i<casos; i++){
		altura = 1;
		cin>>dias;
		for(int j=0; j<dias; j++){
			cin>>actual;
			if(altura != -1){
				if(actual == 1){
					if(anterior == 1){
						altura += 5;
					}
					else{
						altura += 1; 
					}
				}
				else{
					if(anterior == 0){
						altura = -1; 
					}
				}	
			}
			anterior = actual; 	
		}
		cout<<altura<<"\n";
	}
}	
