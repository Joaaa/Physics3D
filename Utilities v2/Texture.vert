#version 330

in vec2 pos;

out vec2 fragPos;

void main(){
	gl_Position = vec4(pos.x, pos.y, 0, 1);
	fragPos = pos;
}