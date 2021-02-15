.386 
.MODEL flat, stdcall 
option casemap:none 
include \masm32\include\windows.inc 
include \masm32\include\kernel32.inc 
include \masm32\include\user32.inc 
includelib \masm32\lib\kernel32.lib 
includelib \masm32\lib\user32.lib 

.DATA 

c@main DD ? 
z@main DD ? 
j@main DD ? 
d@main DD ? 
_9 DD 9
_8 DD 8
a@main DD ? 
k@main DD ? 
_6 DD 6
_5 DD 5
_3 DD 3
m@proced@main DD ? 
_2 DD 2
b@main DD ? 
_0 DD 0

.CODE 

START: 

MOV EAX , _5
MOV a@main , EAX
MOV EAX , _6
MOV b@main , EAX
MOV EAX , _2
MOV k@main , EAX
MOV EAX , _8
instr_8: 
MOV j@main , EAX
CMP a@main , 0
instr_10: 
JG instr_8
MOV EAX , k@main
MOV k@main , EAX
JMP instr_10
MOV EAX , j@main
SUB EAX , _3
MOV j@main , EAX
MOV EAX , j@main
instr_18: 
ADD EAX , k@main
MOV a@main , EAX
MOV EAX , k@main
SUB EAX , _3
MOV k@main , EAX
MOV EAX , a@main
ADD EAX , k@main
MOV EBX , a@main
SUB EBX , j@main
CMP EBX , EAX
JG instr_18
proced@main PROC
MOV EAX , m@proced@main
ADD EAX , k@main
MOV a@main , EAX
RET 
proced@main ENDP
INVOKE proced@main
MOV EAX , b@main
IMUL EAX , c@main
ADD EAX , a@main
MOV EBX , d@main
DIV _9
MOV ECX , EAX
SUB ECX , EBX
MOV z@main , ECX

END START 
