package dev.teelyjc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.logging.Logger;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

import dev.teelyjc.constants.Colors;

public class Main extends JFrame {
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      new Main().setVisible(true);
    });
  }

  private JPanel jPanel;
  private final SimpleUniverse simpleUniverse;

  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public Main() {
    this.initComponents();
    this.setTitle("Desk and PC Setup!");

    Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

    this.simpleUniverse = new SimpleUniverse(c);
    this.simpleUniverse.getViewingPlatform().setNominalViewingTransform();
    this.simpleUniverse.getViewer().getView().setMinimumFrameCycleTime(5);
    this.simpleUniverse.addBranchGraph(this.createSceneGraph());

    this.jPanel.add(c, BorderLayout.CENTER);
  }

  private void initComponents() {
    this.jPanel = new JPanel();

    this.jPanel.setPreferredSize(new Dimension(500, 500));
    this.jPanel.setLayout(new BorderLayout());

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.getContentPane().add(this.jPanel, BorderLayout.CENTER);

    this.pack();
  }

  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();

    Transform3D transform3D = new Transform3D();
    transform3D.rotX(0.25);

    TransformGroup transformGroup = new TransformGroup(transform3D);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    transformGroup.addChild(this.createTableGroup());
    transformGroup.addChild(this.createDesktopPcGroup());
    transformGroup.addChild(this.createMonitorGroup());
    transformGroup.addChild(this.createMouseGroup());
    transformGroup.addChild(this.createKeyboardGroup());

    root.addChild(transformGroup);

    BoundingSphere boundingSphere = new BoundingSphere(new Point3d(0, 0, 0), 1000);

    MouseRotate mouseRotate = new MouseRotate();
    mouseRotate.setTransformGroup(transformGroup);
    mouseRotate.setSchedulingBounds(boundingSphere);
    transformGroup.addChild(mouseRotate);

    MouseTranslate mouseTranslate = new MouseTranslate();
    mouseTranslate.setTransformGroup(transformGroup);
    mouseTranslate.setSchedulingBounds(boundingSphere);
    transformGroup.addChild(mouseTranslate);

    MouseZoom mouseZoom = new MouseZoom();
    mouseZoom.setTransformGroup(transformGroup);
    mouseZoom.setSchedulingBounds(boundingSphere);
    transformGroup.addChild(mouseZoom);

    root.addChild(this.createBackground());
    root.compile();

    return root;
  }

  private TransformGroup createTransformGroupTranslation(Vector3f vector3f) {
    Transform3D transform3D = new Transform3D();
    transform3D.setTranslation(vector3f);

    return new TransformGroup(transform3D);
  }

  private Appearance createAppearance(Color3f color3f, Boolean transparency) {
    Appearance appearance = new Appearance();

    appearance.setColoringAttributes(
        new ColoringAttributes(color3f, ColoringAttributes.SHADE_GOURAUD));

    if (transparency) {
      appearance.setTransparencyAttributes(
          new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.4f));
    }

    return appearance;
  }

  private Background createBackground() {
    Background background = new Background();

    background.setColor(Colors.Gray);
    background.setApplicationBounds(new BoundingSphere());

    return background;
  }

  private TransformGroup createTableGroup() {
    TransformGroup tg = new TransformGroup();

    VectorPrimitive[] tableParts = {
        new VectorPrimitive(
            "Upper Table",
            new Vector3f(0, 0, 0),
            new Box(0.5f, 0.025f, 0.25f, this.createAppearance(Colors.Brown, false))),
        new VectorPrimitive(
            "Left Leg",
            new Vector3f(-0.45f, -0.22f, 0.0f),
            new Box(0.0125f, 0.2f, 0.25f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Right Leg",
            new Vector3f(0.45f, -0.22f, 0.0f),
            new Box(0.0125f, 0.2f, 0.25f, this.createAppearance(Colors.Black, false)))
    };

    for (VectorPrimitive tablePart : tableParts) {
      Main.logger.info(String.format("Initializing for %s at TableParts", tablePart.getName()));

      TransformGroup transformGroup = this.createTransformGroupTranslation(tablePart.getVector3f());
      transformGroup.addChild(tablePart.getPrimitive());

      tg.addChild(transformGroup);
    }

    return tg;
  }

  private TransformGroup createDesktopPcGroup() {
    TransformGroup tg = new TransformGroup();

    VectorPrimitive[] pcParts = {
        new VectorPrimitive(
            "PC Body",
            new Vector3f(0.42f, 0.12f, 0.0f),
            new Box(0.065f, 0.1f, 0.12f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Top Front PC Border",
            new Vector3f(0.42f, 0.22f, 0.12f),
            new Box(0.065f, 0.005f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Top Back PC Border",
            new Vector3f(0.42f, 0.22f, -0.12f),
            new Box(0.065f, 0.005f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Bottom Front PC Border",
            new Vector3f(0.42f, 0.025f, 0.12f),
            new Box(0.065f, 0.005f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Bottom Back PC Border",
            new Vector3f(0.42f, 0.025f, -0.12f),
            new Box(0.065f, 0.005f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Top Left PC Border",
            new Vector3f(0.35f, 0.22f, 0),
            new Box(0.005f, 0.005f, 0.12f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Top Right PC Border",
            new Vector3f(0.49f, 0.22f, 0),
            new Box(0.005f, 0.005f, 0.12f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Bottom Left PC Border",
            new Vector3f(0.35f, 0.025f, 0),
            new Box(0.005f, 0.005f, 0.12f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Bottom Right PC Border",
            new Vector3f(0.49f, 0.025f, 0),
            new Box(0.005f, 0.005f, 0.12f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Front Left PC Border",
            new Vector3f(0.35f, 0.1f, 0.12f),
            new Box(0.005f, 0.12f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Front Right PC Border",
            new Vector3f(0.49f, 0.1f, 0.12f),
            new Box(0.005f, 0.12f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Back Left PC Border",
            new Vector3f(0.35f, 0.1f, -0.12f),
            new Box(0.005f, 0.12f, 0.005f, this.createAppearance(Colors.White, true))),
        new VectorPrimitive(
            "Back Right PC Border",
            new Vector3f(0.49f, 0.1f, -0.12f),
            new Box(0.005f, 0.12f, 0.005f, this.createAppearance(Colors.White, true)))
    };

    for (VectorPrimitive pcPart : pcParts) {
      Main.logger.info(String.format("Initializing for %s at PcParts", pcPart.getName()));

      TransformGroup transformGroup = this.createTransformGroupTranslation(pcPart.getVector3f());
      transformGroup.addChild(pcPart.getPrimitive());

      tg.addChild(transformGroup);
    }

    return tg;
  }

  private TransformGroup createMonitorGroup() {
    TransformGroup tg = new TransformGroup();

    VectorPrimitive[] monitorParts = {
        new VectorPrimitive(
            "Monitor Base",
            new Vector3f(0.0f, 0.03f, -0.1f),
            new Box(0.08f, 0.005f, 0.04f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Monitor Connector",
            new Vector3f(0.0f, 0.08f, -0.1f),
            new Box(0.02f, 0.06f, 0.01f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Monitor Display",
            new Vector3f(0.0f, 0.15f, -0.08f),
            new Box(0.25f, 0.1f, 0.01f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Monitor White Screen",
            new Vector3f(0.0f, 0.15f, -0.07f),
            new Box(0.24f, 0.09f, 0.001f, this.createAppearance(Colors.White, false)))
    };

    for (VectorPrimitive monitorPart : monitorParts) {
      Main.logger.info(String.format("Initializing for %s at MonitorParts", monitorPart.getName()));

      TransformGroup transformGroup = this.createTransformGroupTranslation(monitorPart.getVector3f());
      transformGroup.addChild(monitorPart.getPrimitive());

      tg.addChild(transformGroup);
    }

    return tg;
  }

  private TransformGroup createMouseGroup() {
    TransformGroup tg = new TransformGroup();

    VectorPrimitive[] mouseParts = {
        new VectorPrimitive(
            "Mouse Body",
            new Vector3f(0.25f, 0.03f, 0.1f),
            new Box(0.02f, 0.015f, 0.03f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Mouse Buttons",
            new Vector3f(0.25f, 0.045f, 0.08f),
            new Box(0.015f, 0.0025f, 0.01f, this.createAppearance(Colors.White, false)))
        // Width, Height, ?
    };

    for (VectorPrimitive mousePart : mouseParts) {
      Main.logger.info(String.format("Initializing for %s at MouseParts", mousePart.getName()));

      TransformGroup transformGroup = this.createTransformGroupTranslation(mousePart.getVector3f());
      transformGroup.addChild(mousePart.getPrimitive());

      tg.addChild(transformGroup);
    }

    return tg;
  }

  private TransformGroup createKeyboardGroup() {
    TransformGroup tg = new TransformGroup();

    VectorPrimitive[] keyboardParts = {
        new VectorPrimitive(
            "Keyboard Frame",
            new Vector3f(0.0f, 0.03f, 0.1f),
            new Box(0.1f, 0.005f, 0.05f, this.createAppearance(Colors.Black, false))),
        new VectorPrimitive(
            "Keyboard Buttons",
            new Vector3f(0.0f, 0.035f, 0.1f),
            new Box(0.09f, 0.004f, 0.04f, this.createAppearance(Colors.White, false))),
    };

    for (VectorPrimitive keyboardPart : keyboardParts) {
      Main.logger.info(String.format("Initializing for %s at KeyboardParts", keyboardPart.getName()));

      TransformGroup transformGroup = this.createTransformGroupTranslation(keyboardPart.getVector3f());
      transformGroup.addChild(keyboardPart.getPrimitive());

      tg.addChild(transformGroup);
    }

    return tg;
  }
}
