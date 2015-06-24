package com.epam.aem_training.core.servlets;

import java.awt.Dimension;
import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.ImageHelper;
import com.day.cq.commons.SlingRepositoryException;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.commons.AbstractImageServlet;
import com.day.image.Layer;

/**
 * Renders the image dynamically
 * Usage: /content/geometrixx/en/products/circle.thumbnail.500.500.jpg
 */
@SlingServlet(
		label = "Samples - Sling All Methods Servlet",
		description = "Sample implementation of a Sling All Methods Servlet.",
		methods = { "GET" }, // Ignored if paths is set - Defaults to GET if not specified
		resourceTypes = {"sling/servlet/default"}, // Ignored if paths is set
		selectors = {"resize","thumb","thumbnail"},
		extensions = { "jpg", "png", "gif" }  // Ignored if paths is set
		)
public class DamThumbnailServlet extends AbstractImageServlet {

	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Dimension DEFAULT_DIMENSION = null;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{
		Session session = null;
		try {   
			String[] selectors = request.getRequestPathInfo().getSelectors();
			String extension = request.getRequestPathInfo().getExtension();
			String imagePath = request.getRequestPathInfo().getResourcePath().substring(0, request.getRequestPathInfo().getResourcePath().indexOf("."));

			String type = getImageType(extension);
			if (type == null) {
				response.sendError(404, "Image type not supported");
				return;
			}
			response.setContentType(type);

			ImageContext context = new ImageContext(request, type);       
			session = (Session)request.getResourceResolver().adaptTo(Session.class);

			Resource resource = context.request.getResourceResolver().getResource(imagePath+"."+extension);
			Asset asset = resource.adaptTo(Asset.class);

			Layer layer = ImageHelper.createLayer(session.getNode(imagePath+"."+extension).getSession(), asset.getOriginal().getPath());

			DEFAULT_DIMENSION = new Dimension(layer.getWidth(), layer.getHeight());
			int maxHeight = selectors.length > 1 ? Integer.valueOf(selectors[1]).intValue() : (int)DEFAULT_DIMENSION.getHeight();
			int maxWidth = selectors.length > 2 ? Integer.valueOf(selectors[2]).intValue() : (int)DEFAULT_DIMENSION.getWidth();

			if (layer != null) {
				layer.resize(maxWidth, maxHeight, true);
				applyDiff(layer, context);
			}       
			writeLayer(request, response, context, layer);

		} catch (RepositoryException e) {
			throw new SlingRepositoryException(e);
		}finally{
			if(session != null)
				session.logout();
		}
	}

	@Override
	protected String getImageType(String ext)
	{
		if ("png".equals(ext))
			return "image/png";
		if ("gif".equals(ext))
			return "image/gif";
		if (("jpg".equals(ext)) || ("jpeg".equals(ext))) {
			return "image/jpg";
		}
		return null;
	}

	@Override
	protected Layer createLayer(ImageContext arg0) throws RepositoryException,
	IOException {
		return null;
	}
}