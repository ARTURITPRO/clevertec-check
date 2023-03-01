package edu.clevertec.check.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;


/**
 * The utility for reading a request body
 *
 * @version 1.0
 */
@Slf4j
public class ReaderRequestBody {

    /**
     * Retrieves a body of a request.
     *
     * @param request an {@link HttpServletRequest} object that contains the request the client has made of the servlet.
     * @return a {@link String} retrieved from a request body.
     */
    public static String getRequestBody(final HttpServletRequest request) {

        final StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            if (reader == null) {
                log.debug("Request body could not be read because it's empty");
                return null;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (final Exception e) {
            log.error("Request body could not be read because it's empty", e);
            return null;
        }
    }
}
