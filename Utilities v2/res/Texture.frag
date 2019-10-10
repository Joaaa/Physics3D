#version 330

in vec2 fragPos;

out vec4 outColor;

uniform sampler2D text;

void main(){
	outColor = vec4(texture(text, (fragPos+1)/2).rgb, 1);
	if(abs(outColor.x*256 - 128) < 1 && abs(outColor.y*256 - 104) < 1 && outColor.z < 1){
		outColor.a = 0;
	}
}