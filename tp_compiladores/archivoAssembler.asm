
.MODEL small 

.STACK 200h  

.DATA 

j@main DW  
_9 DW 9
_8 DW 8
a@main DW  
k@main DW  
_6 DW 6
_5 DW 5
_3 DW 3
_2 DW 2
b@main DW  
_0 DW 0
proced@main DW  

.CODE 

START: 

MOV EAX , 5
MOV a@main , EAX
MOV EAX , 6
MOV b@main , EAX
MOV EAX , 2
MOV k@main , EAX
MOV EAX , 8
MOV j@main , EAX
CMP a@main , 0
JG  9
MOV EAX , k@main
MUL EAX , 5
MOV k@main , EAX
JMP 11
MOV EAX , j@main
SUB EAX , 3
MOV j@main , EAX
MOV EAX , j@main
ADD EAX , k@main
MOV a@main , EAX
MOV EAX , k@main
SUB EAX , 3
MOV k@main , EAX
MOV EAX , a@main
ADD EAX , k@main
MOV EBX , a@main
SUB EBX , j@main
CMP EBX , EAX
JG 19
proced@main : 
MOV EAX , m
ADD EAX , k@main
MOV a@main , EAX
RET proced@main 
CALL proced@main
MOV EAX , b@main
MUL EAX , c
ADD EAX , a@main
MOV EBX , d
DIV EBX , 9
MOV ECX , EAX
SUB ECX , EAX

END START 
