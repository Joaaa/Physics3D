#version 140

in vec4 frag_position;
in vec3 frag_normal;

uniform mat4 VP_inv;
uniform vec3 lightDir;
uniform sampler2D shadowTexture;
uniform mat4 shadowMatrix;

out vec4 outColor;

vec3 color = vec3(1, 1, 1);

vec2 poissonDisk[16] = vec2[](
	vec2(-0.8584739, -0.0105699),
	vec2(0.9647728, -0.2201624),
	vec2(0.2068721, 0.8601851),
	vec2(-0.3395929, -0.8932003),
	vec2(-0.7886691, 0.542127),
	vec2(0.5626038, -0.1983825),
	vec2(0.9116812, 0.211909),
	vec2(0.03238156, 0.2609366),
	vec2(0.02710929, -0.3521),
	vec2(0.05757943, -0.7800234),
	vec2(0.5818123, 0.4452237),
	vec2(-0.4980041, -0.2202751),
	vec2(0.4789842, -0.6857864),
	vec2(-0.469608, 0.2983849),
	vec2(-0.2682036, 0.6524032),
	vec2(-0.6392978, -0.595882)
 );

void main(){
    float diffuse = max(0.1, -dot(normalize(frag_normal), lightDir));

    vec3 viewDirection = normalize(vec3(VP_inv * vec4(0.0, 0.0, 0.0, 1.0) - frag_position));

    vec3 specularReflection;
    if (dot(frag_normal, lightDir) > 0.0)  {
      specularReflection = vec3(0.0, 0.0, 0.0);
    } else {
      specularReflection = 0.3 * vec3(1, 1, 1) * pow(max(0.0, dot(reflect(lightDir, frag_normal), viewDirection)), 5);
    }

    vec4 posInShadow = (shadowMatrix*frag_position);
    float depth = posInShadow.z/posInShadow.w;
    vec2 shadowUv = (posInShadow.xy/posInShadow.w+1)/2;
    float shadowDepth;
    float visibility = 1;
    float shadowStrength = 0.4;
    for(int i = 0; i < 16; i++){
        if(depth-((2*texture(shadowTexture, shadowUv + poissonDisk[i]/1000).r)-1) > 0.005*tan(acos(clamp(diffuse, 0, 1)))){
            visibility -= shadowStrength/16;
        }
    }

	outColor = vec4((color*diffuse+specularReflection)*visibility, 1);
}