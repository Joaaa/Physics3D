#version 330 

in vec3 vert;
in vec2 uv;

out vec2 fraguv;

void main(){ 
	gl_Position = vec4(vert.xy, -1, 1);
	fraguv = uv;					 
}