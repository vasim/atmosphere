/*
 * 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007-2008 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 */

package org.atmosphere.samples.rest.counter;

import com.sun.jersey.spi.resource.Singleton;
import org.atmosphere.annotation.Broadcast;
import org.atmosphere.annotation.Resume;
import org.atmosphere.annotation.Suspend;
import org.atmosphere.cpr.AtmosphereHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple application that demonstrate how a Comet long poll request can be implemented.
 * Mainly, the client send a GET, the {@link AtmosphereHandler} suspend the connection. As
 * soon a POST request arrive, the underlying response is resumed.
 *
 * @author jeanfrancoisarcand
 */
@Path("{counter}")
@Singleton
public class RestLongPolling {

    private static final Logger logger = LoggerFactory.getLogger(RestLongPolling.class);

    private final AtomicInteger counter = new AtomicInteger();

    @Suspend
    @GET
    @Produces("text/html;charset=ISO-8859-1")
    public String suspend() {
        return "";
    }


    @POST
    @Path("{counter}")
    @Broadcast(resumeOnBroadcast = true)
    public String increment(@PathParam("counter") String count) {
        logger.info("Broadcasting and resuming: {}", count);
        counter.incrementAndGet();
        return counter.toString();
    }

    @GET
    @Path("/{uuid}")
    @Resume
    public String resume() {
        logger.info("Resuming");
        return "Resumed";
    }

}
