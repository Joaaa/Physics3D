#version 330

in vec2 pos;
in vec2 coord;

out vec2 fragCoords;

void main(){
	gl_Position = vec4(pos.x, pos.y, 0, 1);
	fragCoords = coord;
}