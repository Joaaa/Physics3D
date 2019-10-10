#version 140

in vec3 pos;

out float fragDepth;

uniform mat4 MVP;

void main(){
	vec4 p = MVP * vec4(pos, 1);
	gl_Position = p;
	fragDepth = p.z/p.w;
}