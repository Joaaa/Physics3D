#version 140

in float fragDepth;

out float outColor;

void main(){
	outColor = (fragDepth+1)/2;
}