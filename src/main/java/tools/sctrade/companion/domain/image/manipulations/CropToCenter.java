package tools.sctrade.companion.domain.image.manipulations;

import java.awt.image.BufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.sctrade.companion.domain.image.ImageManipulation;

/**
 * Crops an image to a centered region matching the given aspect ratio. This is useful for ultrawide
 * displays where the game UI is rendered in the center of the screen.
 *
 * <p>
 * If the image already matches or is narrower than the target aspect ratio, it is returned
 * unchanged.
 */
public class CropToCenter implements ImageManipulation {
  private static final Logger logger = LoggerFactory.getLogger(CropToCenter.class);

  private final double targetAspectRatio;

  /**
   * Creates a new CropToCenter manipulation.
   *
   * @param widthRatio the width component of the target aspect ratio (e.g. 16)
   * @param heightRatio the height component of the target aspect ratio (e.g. 9)
   */
  public CropToCenter(double widthRatio, double heightRatio) {
    this.targetAspectRatio = widthRatio / heightRatio;
  }

  @Override
  public BufferedImage manipulate(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    double currentRatio = (double) width / height;

    if (currentRatio <= targetAspectRatio) {
      return image;
    }

    int targetWidth = (int) Math.round(height * targetAspectRatio);
    int x = (width - targetWidth) / 2;

    logger.debug("Cropping {}x{} ({}:1) to centered {}x{} ({}:1)", width, height,
        String.format("%.2f", currentRatio), targetWidth, height,
        String.format("%.2f", targetAspectRatio));

    return image.getSubimage(x, 0, targetWidth, height);
  }
}
