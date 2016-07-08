package cofh.thermaldynamics.render;

import cofh.lib.util.helpers.MathHelper;
import cofh.repack.codechicken.lib.lighting.LightModel;
import cofh.repack.codechicken.lib.render.BlockRenderer.BlockFace;
import cofh.repack.codechicken.lib.render.CCModel;
import cofh.repack.codechicken.lib.render.Vertex5;
import cofh.repack.codechicken.lib.render.uv.UV;
import cofh.repack.codechicken.lib.vec.Cuboid6;
import cofh.repack.codechicken.lib.vec.Transformation;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.core.TDProps;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class ModelHelper
{
  public static class StandardTubes
  {
    public final boolean opaque;
    public final float width;
    static final int[][] orthogs = { { 2, 3, 4, 5 }, { 2, 3, 4, 5 }, { 0, 1, 4, 5 }, { 0, 1, 4, 5 }, { 0, 1, 2, 3 }, { 0, 1, 2, 3 } };
    Cuboid6 center;
    Cuboid6[] pipeWCenter = new Cuboid6[6];
    Cuboid6[] pipe = new Cuboid6[6];
    Cuboid6[] pipeFullLength = new Cuboid6[6];
    
    public static Cuboid6[] rotateCuboids(Cuboid6 paramCuboid6)
    {
      Cuboid6[] arrayOfCuboid6 = new Cuboid6[6];
      for (int i = 0; i < 6; i++) {
        arrayOfCuboid6[i] = paramCuboid6.copy().apply(cofh.repack.codechicken.lib.vec.Rotation.sideRotations[i]);
      }
      return arrayOfCuboid6;
    }
    
    public static CCModel[] genModels(float paramFloat, boolean paramBoolean)
    {
      return genModels(paramFloat, paramBoolean, true);
    }
    
    public static CCModel[] genModels(float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
    {
      StandardTubes localStandardTubes = new StandardTubes(paramFloat, paramBoolean1);
      CCModel[] arrayOfCCModel = new CCModel[64];
      for (int i = 0; i < 64; i++)
      {
        LinkedList localLinkedList = localStandardTubes.createModel(i);
        
        int j = localLinkedList.size();
        arrayOfCCModel[i] = CCModel.newModel(7, j * 2);
        for (int k = 0; k < j; k++) {
          verts[k] = ((Vertex5)localLinkedList.get(k));
        }
        CCModel.generateBackface(arrayOfCCModel[i], 0, arrayOfCCModel[i], j, j);
        
        ModelHelper.finalizeModel(arrayOfCCModel[i], paramBoolean2);
      }
      return arrayOfCCModel;
    }
    
    public StandardTubes(int paramInt)
    {
      width = 0.36F;
      opaque = false;
      double d1 = 0.47D - 0.025D * paramInt;
      double d2 = 0.53D + 0.025D * paramInt;
      double d3 = 0.32D + 0.06D * paramInt;
      double d4 = 0.32D;
      double d5 = 0.68D;
      double[][] arrayOfDouble = { { d1, 0.0D, d1, d2, d4, d2 }, { d1, d3, d1, d2, 1.0D, d2 }, { d4, d4, 0.0D, d5, d3, d4 }, { d4, d4, d5, d5, d3, 1.0D }, { 0.0D, d4, d4, d4, d3, d5 }, { d5, d4, d4, 1.0D, d3, d5 } };
      
      center = new Cuboid6(d4, d4, d4, d5, d3, d5);
      
      pipe = new Cuboid6[6];
      for (int i = 0; i < pipe.length; i++) {
        pipe[i] = new Cuboid6(arrayOfDouble[i][0], arrayOfDouble[i][1], arrayOfDouble[i][2], arrayOfDouble[i][3], arrayOfDouble[i][4], arrayOfDouble[i][5]);
      }
    }
    
    public StandardTubes(float paramFloat, boolean paramBoolean)
    {
      width = paramFloat;
      center = new Cuboid6(-paramFloat, -paramFloat, -paramFloat, paramFloat, paramFloat, paramFloat);
      pipe = rotateCuboids(new Cuboid6(-paramFloat, -0.5D, -paramFloat, paramFloat, -paramFloat, paramFloat));
      pipeWCenter = rotateCuboids(new Cuboid6(-paramFloat, -0.5D, -paramFloat, paramFloat, paramFloat, paramFloat));
      pipeFullLength = rotateCuboids(new Cuboid6(-paramFloat, -0.5D, -paramFloat, paramFloat, 0.5D, paramFloat));
      opaque = paramBoolean;
    }
    
    public LinkedList<Vertex5> createModel(int paramInt)
    {
      LinkedList localLinkedList = new LinkedList();
      for (int i = 0; i < 6; i++)
      {
        int n;
        if ((!opaque) && (MathHelper.isBitSet(paramInt, i)))
        {
          for (n : orthogs[i]) {
            if (MathHelper.isBitSet(paramInt, n)) {
              ModelHelper.addSideFace(localLinkedList, pipe[n], i);
            }
          }
        }
        else
        {
          int j = -1;??? = -1;
          int i2;
          for (i2 : orthogs[i]) {
            if ((MathHelper.isBitSet(paramInt, i2)) && (pipeWCenter[i2] != null))
            {
              j = i2;
              if ((MathHelper.isBitSet(paramInt, i2 ^ 0x1)) && (pipeFullLength[i2] != null) && (pipeFullLength[(i2 ^ 0x1)] != null))
              {
                ??? = i2;
                break;
              }
            }
          }
          if (??? != -1)
          {
            for (i2 : orthogs[i]) {
              if (i2 == ???) {
                ModelHelper.addSideFace(localLinkedList, pipeFullLength[i2], i);
              } else if ((i2 != (??? ^ 0x1)) && (MathHelper.isBitSet(paramInt, i2))) {
                ModelHelper.addSideFace(localLinkedList, pipe[i2], i);
              }
            }
          }
          else if (j != -1)
          {
            for (i2 : orthogs[i]) {
              if (i2 == j) {
                ModelHelper.addSideFace(localLinkedList, pipeWCenter[i2], i);
              } else if (MathHelper.isBitSet(paramInt, i2)) {
                ModelHelper.addSideFace(localLinkedList, pipe[i2], i);
              }
            }
          }
          else
          {
            if (!MathHelper.isBitSet(paramInt, i)) {
              ModelHelper.addSideFace(localLinkedList, center, i);
            }
            for (i2 : orthogs[i]) {
              if (MathHelper.isBitSet(paramInt, i2)) {
                ModelHelper.addSideFace(localLinkedList, pipe[i2], i);
              }
            }
          }
        }
      }
      return localLinkedList;
    }
  }
  
  public static void finalizeModel(CCModel paramCCModel)
  {
    finalizeModel(paramCCModel, true);
  }
  
  public static void finalizeModel(CCModel paramCCModel, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramCCModel.shrinkUVs(9.765625E-4D).computeNormals().computeLighting(LightModel.standardLightModel);
    } else {
      paramCCModel.shrinkUVs(9.765625E-4D).computeNormals();
    }
  }
  
  public static CCModel expandModel(CCModel paramCCModel, double paramDouble)
  {
    return expandModel(paramCCModel, new Cuboid6(-0.5D, -0.5D, -0.5D, 0.5D, 0.5D, 0.5D), paramDouble);
  }
  
  public static CCModel expandModel(CCModel paramCCModel, Cuboid6 paramCuboid6, double paramDouble)
  {
    CCModel localCCModel = CCModel.newModel(vp == 4 ? 7 : 3, verts.length);
    verts = ((Vertex5[])verts.clone());
    for (Vertex5 localVertex5 : verts)
    {
      vec.multiply(paramDouble);
      if (vec.x < min.x) {
        vec.x = min.x;
      }
      if (vec.y < min.y) {
        vec.y = min.y;
      }
      if (vec.z < min.z) {
        vec.z = min.z;
      }
      if (vec.x > max.x) {
        vec.x = max.x;
      }
      if (vec.y > max.y) {
        vec.y = max.y;
      }
      if (vec.z > max.z) {
        vec.z = max.z;
      }
    }
    return localCCModel.computeNormals();
  }
  
  static Vector3[] axes = { new Vector3(0.0D, -1.0D, 0.0D), new Vector3(0.0D, 1.0D, 0.0D), new Vector3(0.0D, 0.0D, -1.0D), new Vector3(0.0D, 0.0D, 1.0D), new Vector3(-1.0D, 0.0D, 0.0D), new Vector3(1.0D, 0.0D, 0.0D) };
  static int[] sideMasks = { 3, 3, 12, 12, 48, 48 };
  
  public static void addSideFaces(LinkedList<Vertex5> paramLinkedList, Cuboid6 paramCuboid6, int paramInt)
  {
    for (int i = 0; i < 6; i++) {
      if ((paramInt & 1 << i) == 0) {
        addSideFace(paramLinkedList, paramCuboid6, i);
      }
    }
  }
  
  public static LinkedList<Vertex5> addSideFace(LinkedList<Vertex5> paramLinkedList, Cuboid6 paramCuboid6, int paramInt)
  {
    face.loadCuboidFace(paramCuboid6.copy().add(Vector3.center), paramInt);
    for (Vertex5 localVertex5 : face.getVertices()) {
      paramLinkedList.add(new Vertex5(vec.copy().sub(Vector3.center), uv.copy()));
    }
    return paramLinkedList;
  }
  
  static BlockRenderer.BlockFace face = new BlockRenderer.BlockFace();
  
  public static LinkedList<Vertex5> apply(LinkedList<Vertex5> paramLinkedList, Transformation paramTransformation)
  {
    LinkedList localLinkedList = new LinkedList();
    for (Vertex5 localVertex5 : paramLinkedList) {
      localLinkedList.add(localVertex5.copy().apply(paramTransformation));
    }
    return localLinkedList;
  }
  
  public static LinkedList<Vertex5> simplifyModel(LinkedList<Vertex5> paramLinkedList)
  {
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator1 = paramLinkedList.iterator();
    Face localFace;
    while (localIterator1.hasNext())
    {
      localObject = Face.loadFromIterator(localIterator1);
      for (localIterator2 = localLinkedList.iterator(); localIterator2.hasNext();)
      {
        localFace = (Face)localIterator2.next();
        if (((Face)localObject).attemptToCombine(localFace)) {
          localIterator2.remove();
        }
      }
      localLinkedList.add(localObject);
    }
    Object localObject = new LinkedList();
    for (Iterator localIterator2 = localLinkedList.iterator(); localIterator2.hasNext();)
    {
      localFace = (Face)localIterator2.next();
      Collections.addAll((Collection)localObject, verts);
    }
    return (LinkedList<Vertex5>)localObject;
  }
  
  public static class Face
  {
    public Vertex5[] verts;
    
    public static Face loadFromIterator(Iterator<Vertex5> paramIterator)
    {
      Face localFace = new Face(new Vertex5[4]);
      for (int i = 0; i < 4; i++) {
        verts[i] = ((Vertex5)paramIterator.next());
      }
      return localFace;
    }
    
    public Vertex5 vec(int paramInt)
    {
      return verts[(paramInt & 0x3)];
    }
    
    public void setVec(int paramInt, Vertex5 paramVertex5)
    {
      verts[(paramInt & 0x3)] = paramVertex5;
    }
    
    public Face(Vertex5... paramVarArgs)
    {
      assert (paramVarArgs.length == 4);
      verts = paramVarArgs;
    }
    
    public boolean isPolygon()
    {
      for (int i = 0; i < 4; i++) {
        if (vecvec.equalsT(vec1vec)) {
          return true;
        }
      }
      return false;
    }
    
    public Face reverse()
    {
      verts = new Vertex5[] { verts[3], verts[2], verts[1], verts[0] };
      return this;
    }
    
    public boolean attemptToCombine(Face paramFace)
    {
      if ((isPolygon()) || (paramFace.isPolygon())) {
        return false;
      }
      if (attemptToCombineUnflipped(paramFace)) {
        return true;
      }
      reverse();
      if (attemptToCombineUnflipped(paramFace)) {
        return true;
      }
      reverse();
      paramFace.reverse();
      if (attemptToCombineUnflipped(paramFace)) {
        return true;
      }
      reverse();
      if (attemptToCombineUnflipped(paramFace)) {
        return true;
      }
      reverse();
      paramFace.reverse();
      return false;
    }
    
    public boolean equalVert(Vertex5 paramVertex51, Vertex5 paramVertex52)
    {
      return (vec.equalsT(vec)) && (uv.equals(uv));
    }
    
    public boolean attemptToCombineUnflipped(Face paramFace)
    {
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if ((equalVert(vec(i), vec(j))) && (equalVert(vec(i + 1), vec(j - 1))))
          {
            Vector3 localVector31 = vec1vec.copy().subtract(vecvec).normalize();
            Vector3 localVector32 = vec2vec.copy().subtract(vec1vec).normalize();
            
            Vector3 localVector33 = vecvec.copy().subtract(vec1vec).normalize();
            Vector3 localVector34 = vec1vec.copy().subtract(vec2vec).normalize();
            if ((localVector31.equalsT(localVector33)) && (localVector32.equalsT(localVector34)))
            {
              setVec(i, paramFace.vec(j + 1));
              setVec(i + 1, paramFace.vec(j - 2));
              return true;
            }
          }
        }
      }
      return false;
    }
  }
  
  public static class OctagonalTubeGen
  {
    double size;
    double innerSize;
    static final double outerWidth = 0.5D;
    boolean frameOnly = false;
    Vector3[] octoFace;
    
    public OctagonalTubeGen(double paramDouble, boolean paramBoolean)
    {
      this(paramDouble, paramDouble * 0.414D, paramBoolean);
    }
    
    public static int getBestSide(Vector3 paramVector3)
    {
      int i = 0;
      double d = 0.0D;
      for (int j = 2; j < 6; j++) {
        if (Math.abs(paramVector3.getSide(j)) > d)
        {
          d = Math.abs(paramVector3.getSide(j));
          i = j;
        }
      }
      return i;
    }
    
    public static Vertex5 toVertex5(Vector3 paramVector3)
    {
      return toVertex5(paramVector3, getBestSide(paramVector3));
    }
    
    public static Vertex5 toVertex5(Vector3 paramVector3, int paramInt)
    {
      UV localUV;
      if ((paramInt == 0) || (paramInt == 1)) {
        localUV = new UV(0.5D + x, 0.5D + z);
      } else if ((paramInt == 2) || (paramInt == 3)) {
        localUV = new UV(0.5D + x, 0.5D + y);
      } else if ((paramInt == 4) || (paramInt == 5)) {
        localUV = new UV(0.5D + z, 0.5D + y);
      } else {
        localUV = new UV(0.5D, 0.5D);
      }
      return new Vertex5(paramVector3, localUV);
    }
    
    public OctagonalTubeGen(double paramDouble1, double paramDouble2, boolean paramBoolean)
    {
      size = paramDouble1;
      innerSize = paramDouble2;
      frameOnly = paramBoolean;
      
      octoFace = new Vector3[8];
      
      octoFace[0] = new Vector3(-paramDouble1, -0.5D, -paramDouble2);
      octoFace[1] = new Vector3(-paramDouble2, -0.5D, -paramDouble1);
      octoFace[2] = new Vector3(paramDouble2, -0.5D, -paramDouble1);
      octoFace[3] = new Vector3(paramDouble1, -0.5D, -paramDouble2);
      octoFace[4] = new Vector3(paramDouble1, -0.5D, paramDouble2);
      octoFace[5] = new Vector3(paramDouble2, -0.5D, paramDouble1);
      octoFace[6] = new Vector3(-paramDouble2, -0.5D, paramDouble1);
      octoFace[7] = new Vector3(-paramDouble1, -0.5D, paramDouble2);
    }
    
    public CCModel generateSideFace()
    {
      CCModel localCCModel = CCModel.newModel(7, 24);
      
      verts[0] = toVertex5(octoFace[0].copy(), 0);
      verts[1] = toVertex5(octoFace[1].copy(), 0);
      verts[2] = toVertex5(octoFace[2].copy(), 0);
      verts[3] = toVertex5(octoFace[3].copy(), 0);
      
      verts[4] = toVertex5(octoFace[4].copy(), 0);
      verts[5] = toVertex5(octoFace[5].copy(), 0);
      verts[6] = toVertex5(octoFace[6].copy(), 0);
      verts[7] = toVertex5(octoFace[7].copy(), 0);
      
      verts[8] = toVertex5(octoFace[0].copy(), 0);
      verts[9] = toVertex5(octoFace[3].copy(), 0);
      verts[10] = toVertex5(octoFace[4].copy(), 0);
      verts[11] = toVertex5(octoFace[7].copy(), 0);
      for (int i = 0; i < 12; i++) {
        verts[i].vec.y = (-0.5D * (frameOnly ? 0.75D : 0.99D));
      }
      CCModel.generateBackface(localCCModel, 0, localCCModel, 12, 12);
      return localCCModel;
    }
    
    public CCModel generateConnection()
    {
      CCModel localCCModel = CCModel.newModel(7, 64);
      double d1 = 0.375D * TDProps.largeInnerModelScaling;
      double d2 = 1.01D;
      for (int i = 0; i < 8; i++)
      {
        verts[(i * 4)] = new Vertex5(octoFace[i].copy().multiply(d2, 1.0D, d2), 0.5D - innerSize, 0.0D);
        verts[(i * 4 + 1)] = new Vertex5(octoFace[i].copy().multiply(d2, 1.0D, d2).setSide(0, -d1), 0.5D - innerSize, 0.5D - d1);
        verts[(i * 4 + 2)] = new Vertex5(octoFace[((i + 1) % 8)].copy().multiply(d2, 1.0D, d2).setSide(0, -d1), 0.5D + innerSize, 0.5D - d1);
        verts[(i * 4 + 3)] = new Vertex5(octoFace[((i + 1) % 8)].copy().multiply(d2, 1.0D, d2), 0.5D + innerSize, 0.0D);
      }
      CCModel.generateBackface(localCCModel, 0, localCCModel, 32, 32);
      return localCCModel;
    }
    
    public CCModel[] generateModels()
    {
      CCModel[] arrayOfCCModel = new CCModel[76];
      for (int i = 0; i < 64; i++)
      {
        LinkedList localLinkedList = generateIntersections(i);
        localLinkedList = ModelHelper.simplifyModel(localLinkedList);
        int j = localLinkedList.size();
        arrayOfCCModel[i] = CCModel.newModel(7, j * 2);
        for (int k = 0; k < j; k++)
        {
          Vertex5 localVertex5 = (Vertex5)localLinkedList.get(k);
          verts[k] = localVertex5;
        }
        CCModel.generateBackface(arrayOfCCModel[i], 0, arrayOfCCModel[i], j, j);
        ModelHelper.finalizeModel(arrayOfCCModel[i]);
      }
      arrayOfCCModel[64] = generateConnection();
      for (i = 0; i < 6; i++)
      {
        if (i != 0) {
          arrayOfCCModel[(64 + i)] = arrayOfCCModel[64].sidedCopy(0, i, Vector3.zero);
        }
        ModelHelper.finalizeModel(arrayOfCCModel[(64 + i)]);
      }
      arrayOfCCModel[70] = generateSideFace();
      for (i = 0; i < 6; i++)
      {
        if (i != 0) {
          arrayOfCCModel[(70 + i)] = arrayOfCCModel[70].sidedCopy(0, i, Vector3.zero);
        }
        ModelHelper.finalizeModel(arrayOfCCModel[(70 + i)]);
      }
      return arrayOfCCModel;
    }
    
    public LinkedList<Vertex5> generateIntersections(int paramInt)
    {
      LinkedList localLinkedList1 = new LinkedList();
      
      LinkedList localLinkedList2 = ModelHelper.addSideFace(new LinkedList(), new Cuboid6(-innerSize, -size, -innerSize, innerSize, size, innerSize), 0);
      LinkedList localLinkedList3 = new LinkedList();
      for (Vector3 localVector31 = 0; localVector31 < 8; localVector31++) {
        if ((!frameOnly) || (localVector31 % 2 != 0))
        {
          localLinkedList3.add(toVertex5(octoFace[localVector31].copy()));
          localLinkedList3.add(toVertex5(octoFace[localVector31].copy().setSide(0, -size)));
          localLinkedList3.add(toVertex5(octoFace[((localVector31 + 1) % 8)].copy().setSide(0, -size)));
          localLinkedList3.add(toVertex5(octoFace[((localVector31 + 1) % 8)].copy()));
        }
      }
      for (localVector31 = 0; localVector31 < 6; localVector31++) {
        if ((paramInt & 1 << localVector31) != 0) {
          localLinkedList1.addAll(ModelHelper.apply(localLinkedList3, cofh.repack.codechicken.lib.vec.Rotation.sideRotations[localVector31]));
        } else {
          localLinkedList1.addAll(ModelHelper.apply(localLinkedList2, cofh.repack.codechicken.lib.vec.Rotation.sideRotations[localVector31]));
        }
      }
      Vector3 localVector32;
      Vector3 localVector33;
      int i;
      for (localVector31 = 0; localVector31 < 6; localVector31++) {
        for (localVector32 = localVector31 + 1; localVector32 < 6; localVector32++) {
          if ((localVector31 ^ 0x1) != localVector32)
          {
            localVector33 = (paramInt & 1 << localVector31) != 0 ? 1 : 0;
            i = (paramInt & 1 << localVector32) != 0 ? 1 : 0;
            
            Vector3 localVector34 = ModelHelper.axes[localVector31].copy();
            Vector3 localVector35 = ModelHelper.axes[localVector32].copy();
            Vector3 localVector36 = localVector34.copy().crossProduct(localVector35);
            if ((localVector33 == 0) && (i == 0))
            {
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(innerSize)).add(localVector36.copy().multiply(innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(innerSize)).add(localVector36.copy().multiply(-innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(innerSize).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(-innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(innerSize).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(innerSize)), localVector31));
            }
            else if ((localVector33 == 0) && (i != 0))
            {
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(innerSize)).add(localVector36.copy().multiply(innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(innerSize)).add(localVector36.copy().multiply(-innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(-innerSize)), localVector31));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(innerSize)), localVector31));
            }
            else if ((localVector33 != 0) && (i == 0))
            {
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(innerSize)), localVector32));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(size).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(-innerSize)), localVector32));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(innerSize).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(-innerSize)), localVector32));
              localLinkedList1.add(toVertex5(localVector34.copy().multiply(innerSize).add(localVector35.copy().multiply(size)).add(localVector36.copy().multiply(innerSize)), localVector32));
            }
          }
        }
      }
      if (!frameOnly) {
        for (localVector31 = 0; localVector31 < 2; localVector31++) {
          for (localVector32 = 2; localVector32 < 4; localVector32++) {
            for (localVector33 = 4; localVector33 < 6; localVector33++)
            {
              i = (paramInt & 1 << localVector31) != 0 ? 1 : 0;
              int j = (paramInt & 1 << localVector32) != 0 ? 1 : 0;
              int k = (paramInt & 1 << localVector33) != 0 ? 1 : 0;
              
              int m = (i != 0 ? 1 : 0) + (j != 0 ? 1 : 0) + (k != 0 ? 1 : 0);
              
              Vector3 localVector37 = ModelHelper.axes[localVector31];
              Vector3 localVector38 = ModelHelper.axes[localVector32];
              Vector3 localVector39 = ModelHelper.axes[localVector33];
              Vector3 localVector310;
              Vector3 localVector311;
              Vector3 localVector312;
              if (m == 3)
              {
                localVector310 = localVector37.copy().multiply(size).add(localVector38.copy().multiply(size).add(localVector39.copy().multiply(innerSize)));
                localVector311 = localVector37.copy().multiply(size).add(localVector38.copy().multiply(innerSize).add(localVector39.copy().multiply(size)));
                localVector312 = localVector37.copy().multiply(innerSize).add(localVector38.copy().multiply(size).add(localVector39.copy().multiply(size)));
                
                localLinkedList1.add(toVertex5(localVector310, localVector31));
                localLinkedList1.add(toVertex5(localVector312, localVector31));
                localLinkedList1.add(toVertex5(localVector311, localVector31));
                localLinkedList1.add(toVertex5(localVector310, localVector31));
              }
              else if (m == 0)
              {
                localVector310 = localVector37.copy().multiply(size).add(localVector38.copy().multiply(innerSize).add(localVector39.copy().multiply(innerSize)));
                localVector311 = localVector37.copy().multiply(innerSize).add(localVector38.copy().multiply(innerSize).add(localVector39.copy().multiply(size)));
                localVector312 = localVector37.copy().multiply(innerSize).add(localVector38.copy().multiply(size).add(localVector39.copy().multiply(innerSize)));
                
                localLinkedList1.add(toVertex5(localVector310, 0));
                localLinkedList1.add(toVertex5(localVector312, 0));
                localLinkedList1.add(toVertex5(localVector311, 0));
                localLinkedList1.add(toVertex5(localVector310, 0));
              }
              else if (m == 1)
              {
                if (i != 0)
                {
                  localVector310 = localVector37;
                  localVector311 = localVector38;
                  localVector312 = localVector39;
                }
                else if (j != 0)
                {
                  localVector310 = localVector38;
                  localVector311 = localVector37;
                  localVector312 = localVector39;
                }
                else
                {
                  localVector310 = localVector39;
                  localVector311 = localVector37;
                  localVector312 = localVector38;
                }
                localLinkedList1.add(toVertex5(localVector310.copy().multiply(innerSize).add(localVector311.copy().multiply(size)).add(localVector312.copy().multiply(innerSize)), 0));
                localLinkedList1.add(toVertex5(localVector310.copy().multiply(size).add(localVector311.copy().multiply(size).add(localVector312.copy().multiply(innerSize))), 0));
                localLinkedList1.add(toVertex5(localVector310.copy().multiply(size).add(localVector311.copy().multiply(innerSize)).add(localVector312.copy().multiply(size)), 0));
                localLinkedList1.add(toVertex5(localVector310.copy().multiply(innerSize).add(localVector311.copy().multiply(innerSize)).add(localVector312.copy().multiply(size)), 0));
              }
              else if (m == 2)
              {
                Vector3 localVector313;
                if (i == 0)
                {
                  localVector310 = localVector31;
                  localVector311 = localVector37;
                  localVector312 = localVector38;
                  localVector313 = localVector39;
                }
                else if (j == 0)
                {
                  localVector310 = localVector32;
                  localVector311 = localVector38;
                  localVector312 = localVector37;
                  localVector313 = localVector39;
                }
                else
                {
                  localVector310 = localVector33;
                  localVector311 = localVector39;
                  localVector312 = localVector37;
                  localVector313 = localVector38;
                }
                localLinkedList1.add(toVertex5(localVector311.copy().multiply(size).add(localVector312.copy().multiply(innerSize)).add(localVector313.copy().multiply(innerSize)), localVector310));
                localLinkedList1.add(toVertex5(localVector311.copy().multiply(size).add(localVector312.copy().multiply(size)).add(localVector313.copy().multiply(innerSize)), localVector310));
                localLinkedList1.add(toVertex5(localVector311.copy().multiply(innerSize).add(localVector312.copy().multiply(size)).add(localVector313.copy().multiply(size)), localVector310));
                localLinkedList1.add(toVertex5(localVector311.copy().multiply(size).add(localVector312.copy().multiply(innerSize)).add(localVector313.copy().multiply(size)), localVector310));
              }
            }
          }
        }
      }
      return localLinkedList1;
    }
  }
  
  static int[][] orthogonals = { { 6, 6, 4, 5, 2, 3 }, { 6, 6, 4, 5, 2, 3 }, { 4, 5, 6, 6, 0, 1 }, { 5, 4, 6, 6, 1, 0 }, { 2, 3, 0, 1, 6, 6 }, { 3, 2, 1, 0, 6, 6 } };
  static int[][] edgePairs = { { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 2, 4 }, { 2, 5 }, { 3, 4 }, { 3, 5 } };
  static int[][] cornerTriplets = { { 0, 2, 4 }, { 0, 2, 5 }, { 0, 3, 4 }, { 0, 3, 5 }, { 1, 2, 4 }, { 1, 2, 5 }, { 1, 3, 4 }, { 1, 3, 5 } };
  static int[][] orthogAxes = { { 2, 4 }, { 2, 4 }, { 0, 4 }, { 0, 4 }, { 0, 2 }, { 0, 2 } };
  
  public static class SideTubeGen
  {
    public double s;
    public double s2;
    public double h = 1.0D;
    public static CCModel[] standardTubes = new SideTubeGen(0.1876D).generateModels();
    public static CCModel[] standardTubesInner = new SideTubeGen(0.1876D).contract(0.999D).generateModels();
    
    public SideTubeGen(double paramDouble)
    {
      this(paramDouble, paramDouble + 0.09375D);
    }
    
    public SideTubeGen(double paramDouble1, double paramDouble2)
    {
      s = paramDouble1;
      s2 = paramDouble2;
    }
    
    public SideTubeGen contract(double paramDouble)
    {
      h = paramDouble;
      return this;
    }
    
    public Cuboid6 newCube(Vector3 paramVector31, Vector3 paramVector32)
    {
      double d;
      if (x > x)
      {
        d = x;
        x = x;
        x = d;
      }
      if (y > y)
      {
        d = y;
        y = y;
        y = d;
      }
      if (z > z)
      {
        d = z;
        z = z;
        z = d;
      }
      if (h < 1.0D)
      {
        Vector3 localVector3 = paramVector31.copy().add(paramVector32).multiply(0.5D);
        
        x = ((x <= -0.5D) || (x >= 0.5D) ? x : (x - x) * h + x);
        y = ((y <= -0.5D) || (y >= 0.5D) ? y : (y - y) * h + y);
        z = ((z <= -0.5D) || (z >= 0.5D) ? z : (z - z) * h + z);
        x = ((x <= -0.5D) || (x >= 0.5D) ? x : (x - x) * h + x);
        y = ((y <= -0.5D) || (y >= 0.5D) ? y : (y - y) * h + y);
        z = ((z <= -0.5D) || (z >= 0.5D) ? z : (z - z) * h + z);
      }
      return new Cuboid6(paramVector31, paramVector32);
    }
    
    private LinkedList<Vertex5> generateConnections(int paramInt)
    {
      LinkedList localLinkedList = new LinkedList();
      
      Vector3 localVector31 = ModelHelper.axes[paramInt];
      Vector3 localVector32 = ModelHelper.axes[ModelHelper.orthogAxes[paramInt][0]];
      Vector3 localVector33 = ModelHelper.axes[ModelHelper.orthogAxes[paramInt][1]];
      int j;
      Cuboid6 localCuboid6;
      for (int i = -1; i <= 1; i += 2) {
        for (j = -1; j <= 1; j += 2)
        {
          localCuboid6 = newCube(localVector31.copy().multiply(s2).add(localVector32.copy().multiply(s * i)).add(localVector33.copy().multiply(s * j)), localVector31
            .copy().multiply(h / 2.0D).add(localVector32.copy().multiply(s2 * i)).add(localVector33.copy().multiply(s2 * j)));
          ModelHelper.addSideFaces(localLinkedList, localCuboid6, 1 << paramInt ^ 0x3F);
        }
      }
      for (i = 0; i < 6; i++) {
        if ((paramInt != i) && ((paramInt ^ 0x1) != i))
        {
          localVector31 = ModelHelper.axes[paramInt];
          localVector32 = ModelHelper.axes[i];
          j = ModelHelper.orthogonals[paramInt][i];
          localVector33 = ModelHelper.axes[j];
          
          localCuboid6 = newCube(localVector31.copy().multiply(h / 2.0D - (s2 - s)).add(localVector32.copy().multiply(s)).add(localVector33.copy().multiply(s)), localVector31
            .copy().multiply(h / 2.0D).add(localVector32.copy().multiply(s2)).add(localVector33.copy().multiply(-s)));
          
          ModelHelper.addSideFaces(localLinkedList, localCuboid6, 1 << j | 1 << (j ^ 0x1));
        }
      }
      return localLinkedList;
    }
    
    private LinkedList<Vertex5> generateIntersections(int paramInt)
    {
      LinkedList localLinkedList = new LinkedList();
      
      int i = 0;
      Vector3 localVector31;
      Vector3 localVector32;
      Vector3 localVector33;
      int k;
      int m;
      Cuboid6 localCuboid6;
      for (int j = 0; j < 6; j++) {
        if ((paramInt & 1 << j) != 0)
        {
          localVector31 = ModelHelper.axes[j];
          localVector32 = ModelHelper.axes[ModelHelper.orthogAxes[j][0]];
          localVector33 = ModelHelper.axes[ModelHelper.orthogAxes[j][1]];
          for (k = -1; k <= 1; k += 2) {
            for (m = -1; m <= 1; m += 2)
            {
              localCuboid6 = newCube(localVector31.copy().multiply(s2).add(localVector32.copy().multiply(s * k)).add(localVector33.copy().multiply(s * m)), localVector31
                .copy().multiply(h / 2.0D).add(localVector32.copy().multiply(s2 * k)).add(localVector33.copy().multiply(s2 * m)));
              ModelHelper.addSideFaces(localLinkedList, localCuboid6, 1 << j | 1 << (j ^ 0x1));
            }
          }
        }
      }
      int[] arrayOfInt1;
      int n;
      for (arrayOfInt1 : ModelHelper.edgePairs) {
        if (i == 0)
        {
          if (((paramInt & 1 << arrayOfInt1[0]) != 0 ? 1 : 0) != ((paramInt & 1 << arrayOfInt1[1]) != 0 ? 1 : 0)) {}
        }
        else
        {
          localVector31 = ModelHelper.axes[arrayOfInt1[0]];
          localVector32 = ModelHelper.axes[arrayOfInt1[1]];
          n = ModelHelper.orthogonals[arrayOfInt1[0]][arrayOfInt1[1]];
          localVector33 = ModelHelper.axes[n];
          localCuboid6 = newCube(localVector31.copy().multiply(s).add(localVector32.copy().multiply(s)).add(localVector33.copy().multiply(s)), localVector31.copy().multiply(s2).add(localVector32.copy().multiply(s2))
            .add(localVector33.copy().multiply(-s)));
          
          ModelHelper.addSideFaces(localLinkedList, localCuboid6, 1 << n | 1 << (n ^ 0x1));
        }
      }
      for (arrayOfInt1 : ModelHelper.cornerTriplets)
      {
        localVector31 = ModelHelper.axes[arrayOfInt1[0]];
        localVector32 = ModelHelper.axes[arrayOfInt1[1]];
        localVector33 = ModelHelper.axes[arrayOfInt1[2]];
        localCuboid6 = newCube(localVector31.copy().multiply(s).add(localVector32.copy().multiply(s)).add(localVector33.copy().multiply(s)), localVector31
          .copy().multiply(s2).add(localVector32.copy().multiply(s2)).add(localVector33.copy().multiply(s2)));
        
        n = 1 << arrayOfInt1[0] & paramInt | 1 << arrayOfInt1[1] & paramInt | 1 << arrayOfInt1[2] & paramInt;
        if (i == 0)
        {
          if (((paramInt & 1 << arrayOfInt1[1]) != 0 ? 1 : 0) != ((paramInt & 1 << arrayOfInt1[2]) != 0 ? 1 : 0)) {}
        }
        else {
          n |= 1 << (arrayOfInt1[0] ^ 0x1);
        }
        if (i == 0)
        {
          if (((paramInt & 1 << arrayOfInt1[0]) != 0 ? 1 : 0) != ((paramInt & 1 << arrayOfInt1[2]) != 0 ? 1 : 0)) {}
        }
        else {
          n |= 1 << (arrayOfInt1[1] ^ 0x1);
        }
        if (i == 0)
        {
          if (((paramInt & 1 << arrayOfInt1[0]) != 0 ? 1 : 0) != ((paramInt & 1 << arrayOfInt1[1]) != 0 ? 1 : 0)) {}
        }
        else {
          n |= 1 << (arrayOfInt1[2] ^ 0x1);
        }
        ModelHelper.addSideFaces(localLinkedList, localCuboid6, n);
      }
      return localLinkedList;
    }
    
    public CCModel[] generateModels()
    {
      CCModel[] arrayOfCCModel = new CCModel[70];
      LinkedList localLinkedList;
      int j;
      int k;
      Vertex5 localVertex5;
      for (int i = 0; i < 64; i++)
      {
        localLinkedList = generateIntersections(i);
        localLinkedList = ModelHelper.simplifyModel(localLinkedList);
        j = localLinkedList.size();
        arrayOfCCModel[i] = CCModel.newModel(7, j * 2);
        for (k = 0; k < j; k++)
        {
          localVertex5 = (Vertex5)localLinkedList.get(k);
          verts[k] = localVertex5;
        }
        CCModel.generateBackface(arrayOfCCModel[i], 0, arrayOfCCModel[i], j, j);
        
        ModelHelper.finalizeModel(arrayOfCCModel[i]);
      }
      for (i = 0; i < 6; i++)
      {
        localLinkedList = generateConnections(i);
        localLinkedList = ModelHelper.simplifyModel(localLinkedList);
        j = localLinkedList.size();
        arrayOfCCModel[(64 + i)] = CCModel.newModel(7, j);
        for (k = 0; k < j; k++)
        {
          localVertex5 = (Vertex5)localLinkedList.get(k);
          64verts[k] = localVertex5;
        }
        ModelHelper.finalizeModel(arrayOfCCModel[(64 + i)]);
      }
      return arrayOfCCModel;
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\ModelHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */