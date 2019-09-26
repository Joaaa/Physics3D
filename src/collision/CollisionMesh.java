package collision;

import Utilities.Vector4f;
import rendering.WorldObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CollisionMesh extends CollisionModel {

    private final List<Vector4f> corners;
    private final List<MeshTriangle> faces;

    public CollisionMesh(WorldObject worldObject, List<Vector4f> corners, List<MeshTriangle> faces) {
        super(worldObject);
        this.corners = corners;
        this.faces = faces;
    }

    public List<Vector4f> getTransformedCorners() {
        return corners.stream().map(c -> c.leftMult(getPosition().getTransformation())).collect(Collectors.toList());
    }

    public List<MeshTriangle> getTransformedFaces() {
        return faces.stream().map(f -> f.transform(getPosition().getTransformation())).collect(Collectors.toList());
    }

    @Override
    public CollisionResult checkCollision(CollisionModel other) {
        return other.checkCollisionWith(this);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionMesh mesh) {
        return CollisionChecker.checkMeshMeshCollision(this, mesh);
    }

    @Override
    public CollisionResult checkCollisionWith(CollisionSphere sphere) {
        return CollisionChecker.checkMeshSphereCollision(this, sphere);
    }

    public static CollisionMesh createCube(WorldObject o, float width) {
        return createSimple(o, width, width, width);
    }

    public static CollisionMesh createSimple(WorldObject o, float xSize, float ySize, float zSize) {
        List<Vector4f> corners = Arrays.asList(
                new Vector4f(-xSize/2f, -ySize/2f, -zSize/2f, 1),
                new Vector4f(xSize/2f, -ySize/2f, -zSize/2f, 1),
                new Vector4f(-xSize/2f, ySize/2f, -zSize/2f, 1),
                new Vector4f(xSize/2f, ySize/2f, -zSize/2f, 1),
                new Vector4f(-xSize/2f, -ySize/2f, zSize/2f, 1),
                new Vector4f(xSize/2f, -ySize/2f, zSize/2f, 1),
                new Vector4f(-xSize/2f, ySize/2f, zSize/2f, 1),
                new Vector4f(xSize/2f, ySize/2f, zSize/2f, 1)
        );
        List<MeshTriangle> triangles = Arrays.asList(
                createTriangle(corners.get(0), corners.get(1), corners.get(2)),
                createTriangle(corners.get(3), corners.get(1), corners.get(2)),
                createTriangle(corners.get(0), corners.get(1), corners.get(4)),
                createTriangle(corners.get(5), corners.get(1), corners.get(4)),
                createTriangle(corners.get(0), corners.get(2), corners.get(4)),
                createTriangle(corners.get(6), corners.get(2), corners.get(4)),
                createTriangle(corners.get(2), corners.get(3), corners.get(6)),
                createTriangle(corners.get(7), corners.get(3), corners.get(6)),
                createTriangle(corners.get(1), corners.get(3), corners.get(5)),
                createTriangle(corners.get(7), corners.get(3), corners.get(5)),
                createTriangle(corners.get(4), corners.get(5), corners.get(6)),
                createTriangle(corners.get(7), corners.get(5), corners.get(6))
        );

        return new CollisionMesh(o, corners, triangles);
    }

    private static MeshTriangle createTriangle(Vector4f p1, Vector4f p2, Vector4f p3) {
        MeshTriangle triangle = new MeshTriangle(p1, p2, p3);
        if(p1.add(new Vector4f(0, 0, 0, 1).getInverted()).dotProduct(triangle.getNormal()) < 0)
            triangle = new MeshTriangle(p1, p3, p2);
        return triangle;
    }

}
