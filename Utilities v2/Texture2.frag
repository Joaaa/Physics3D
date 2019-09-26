#version 330

in vec2 fragCoords;

out vec4 outColor;

uniform samplerBuffer text;
uniform int width;
uniform int height;

void main(){
	int tx = int(fragCoords.x*width);
	int ty = int(fragCoords.y*height);
	outColor = vec4(texelFetch(text, tx+ty*width).rgb, 1);
	if(abs(outColor.x*256 - 128) < 1 && abs(outColor.y*256 - 104) < 1 && outColor.z < 1){
		outColor.a = 0;
	}
}