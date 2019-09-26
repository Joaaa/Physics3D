#version 330

in vec2 fraguv;

out vec4 out_color;

uniform sampler2D text;
uniform vec3 color;

void main(){
	vec4 tc = texture(text, fraguv);
	/*if(out_color.x > 0.5 && out_color.y > 0.5 && out_color.z > 0.5){
		out_color.a = 0;
	}*/
	out_color = vec4(color, 1-tc.x);					
} 