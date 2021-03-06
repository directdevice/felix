/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.http.base.internal.service.listener;

import java.util.Iterator;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * The <code>ProxyListener</code> implements the Servlet API 2.4 listener
 * interfaces forwarding any event calls to registered OSGi services
 * implementing the respective Servlet API 2.4 listener interface.
 * @deprecated
 */
@Deprecated
public class HttpSessionAttributeListenerManager extends AbstractListenerManager<HttpSessionAttributeListener>
    implements HttpSessionAttributeListener
{
    private static org.osgi.framework.Filter createFilter(final BundleContext btx)
    {
        try
        {
            return btx.createFilter(String.format("(&(objectClass=%s)(!(%s=*)))",
                    HttpSessionAttributeListener.class.getName(),
                    HttpWhiteboardConstants.HTTP_WHITEBOARD_LISTENER));
        }
        catch ( final InvalidSyntaxException ise)
        {
            // we can safely ignore it as the above filter is a constant
        }
        return null; // we never get here - and if we get an NPE which is fine
    }

    public HttpSessionAttributeListenerManager(BundleContext context)
    {
        super(context, createFilter(context));
    }

    @Override
    public void attributeAdded(final HttpSessionBindingEvent se)
    {
        final Iterator<HttpSessionAttributeListener> listeners = getContextListeners();
        while (listeners.hasNext())
        {
            listeners.next().attributeAdded(se);
        }
    }

    @Override
    public void attributeRemoved(final HttpSessionBindingEvent se)
    {
        final Iterator<HttpSessionAttributeListener> listeners = getContextListeners();
        while (listeners.hasNext())
        {
            listeners.next().attributeRemoved(se);
        }
    }

    @Override
    public void attributeReplaced(final HttpSessionBindingEvent se)
    {
        final Iterator<HttpSessionAttributeListener> listeners = getContextListeners();
        while (listeners.hasNext())
        {
            listeners.next().attributeReplaced(se);
        }
    }
}
