#version 330

in vec2 pos;

void main(){ 
	gl_Position = vec4(pos, -1, 1);
}