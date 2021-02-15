.386 
.MODEL flat, stdcall 
option casemap:none 
include \masm32\include\windows.inc 
include \masm32\include\kernel32.inc 
include \masm32\include\user32.inc 
includelib \masm32\lib\kernel32.lib 
includelib \masm32\lib\user32.lib 

.DATA 

y@suma@main DD ? 
c@main DD ? 
z@main DD ? 
_25 DD 25
x@suma@main DD ? 
yes@suma@main DD ? 
_8 DD 8
_5_ DQ 5.
_2_5 DQ 2.5
a@main DD ? 
_3 DD 3
b@main DQ ? 
_0 DD 0
bb@main DQ ? 

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
suma@main PROC
MOV EAX , x@suma@main
ADD EAX , y@suma@main
MOV yes@suma@main , EAX
RET 
suma@main ENDP
INVOKE suma@main
MOV EAX , a@main
ADD EAX , c@main
ADD EAX , _8
MOV z@main , EAX

END START 
