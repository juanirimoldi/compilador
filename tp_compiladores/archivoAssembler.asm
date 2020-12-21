
.MODEL small 

.STACK 200h  

.DATA 

k@main@main DW  
fff@main DW  
_1000 DW 1000
_99 DW 99
fn@f2@main DW  
ww@main DW  
f2a@f2@main DW  
_100 DW 100
proced@main DW  
f1@main DW  
x@main DW  
f2@main DW  
a@main DW  
y@main DW  
b@main DW  
f1a@f1@main DW  
j@main@main DW  
_9 DW 9
z@main DW  
_8 DW 8
_89 DW 89
c@main DW  
_5 DW 5
k@main@main DW  
yyy@proced@main DW  
j@main@main DW  
_3 DW 3
fnnn@fn@f2@main DW  
_2 DW 2
_1 DW 1
_22 DW 22
_0 DW 0
d@main DW  

.CODE 

START: 

MOV EAX , 99
ADD EAX , 3
MOV y@main , EAX
MOV EAX , 1
MOV z@main , EAX
MOV EAX , y@main
ADD EAX , z@main
MOV x@main , EAX
MOV EAX , x@main
ADD EAX , 5
CMP EAX , 9
JL  8
MOV EAX , x@main
ADD EAX , 5
CMP EAX , 9
JL  12
MOV EAX , 8
MOV fff@main , EAX
MOV EAX , 8
MOV ww@main , EAX
CMP ww@main , 0
JG   
CMP b1 , 8
JG  22
MOV EAX , b
MOV y@main , EAX
MOV EAX , 8
MUL EAX , y@main
ADD EAX , k@main
MOV k@main , EAX
JMP 24
MOV EAX , j@main
SUB EAX , 3
MOV j@main , EAX
CMP ww@main , 0
JL  29
MOV EAX , k@main@main
MUL EAX , y@main
MOV k@main@main , EAX
JMP 34
CMP b2 , 8
JG  32
MOV EAX , b
MOV y@main , EAX
MOV EAX , j@main@main
SUB EAX , 3
MOV j@main@main , EAX
MOV EAX , 5
MOV a@main , EAX
MOV EAX , 8
MOV b@main , EAX
MOV EAX , 9
MOV c@main , EAX
MOV EAX , a@main
SUB EAX , b@main
MOV EBX , c@main
ADD EBX , 1
CMP EBX , EAX
JG  44
MOV EAX , b@main
ADD EAX , c@main
MOV a@main , EAX
JMP 46
MOV EAX , b@main
SUB EAX , c@main
MOV a@main , EAX
MOV EAX , 2
MOV d@main , EAX
MOV EAX , b@main
ADD EAX , c@main
MOV a@main , EAX
MOV EAX , d@main
SUB EAX , 3
MOV c@main , EAX
MOV EAX , a@main
ADD EAX , b@main
MOV EBX , c@main
SUB EBX , d@main
CMP EBX , EAX
JG 55
proced@main : 
MOV EAX , 99
ADD EAX , m
MOV yyy@proced@main , EAX
RET proced@main 
f1@main : 
MOV EAX , y@main
ADD EAX , z@main
MOV x@main , EAX
MOV EAX , 100
MOV f1a@f1@main , EAX
RET f1@main 
f2@main : 
MOV EAX , y@main
ADD EAX , z@main
MOV x@main , EAX
MOV EAX , 1000
MOV f2a@f2@main , EAX
fn@f2@main : 
MOV EAX , x@main
MOV fnnn@fn@f2@main , EAX
RET fn@f2@main 
RET  
CALL proced@main
CALL proced@main
CALL proced@main
MOV EAX , 22
ADD EAX , a@main
MOV b@main , EAX
MOV EAX , a@main
SUB EAX , b@main
MOV c@main , EAX

END START 
