.386 
.MODEL flat, stdcall 
option casemap:none 
include \masm32\include\windows.inc 
include \masm32\include\kernel32.inc 
include \masm32\include\user32.inc 
includelib \masm32\lib\kernel32.lib 
includelib \masm32\lib\user32.lib 

.DATA 

c@main DW ? 
z@main DW ? 
_25 DW 25
_8 DW 8
_5_ DQ 5.
_2_5 DQ 2.5
a@main DW ? 
_2_ DQ 2.
_88_ DQ 88.
_3 DW 3
b@main DW ? 
_0 DW 0
bb@main DW ? 

.CODE 

START: 

MOV EAX , _25
MOV a@main , EAX
FLD _2_5
FSTP b@main
FLD _5_
FSTP bb@main
FADD 
FST b@main
FLD _2_
FLD _88_
FADD 
FST bb@main
PROC power@main
MOV EAX , x
ADD EAX , y
MOV a@main , EAX
RET 
ENDP power@main
INVOKE power@main
MOV EAX , b@main
MUL EAX , 8
MUL EAX , c@main
ADD EAX , a@main
MOV z@main , EAX
MOV z@main , EAX

END START 
