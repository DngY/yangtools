/*
 * Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.yangtools.yang.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.opendaylight.yangtools.yang.common.RpcError.ErrorSeverity;
import org.opendaylight.yangtools.yang.common.RpcError.ErrorType;

/**
 * A builder for creating RpcResult instances.
 *
 * @author Thomas Pantelis
 *
 * @param <T> the result value type
 */
public class RpcResultBuilder<T> {

    private static class RpcResultImpl<T> implements RpcResult<T> {

        private final Collection<RpcError> errors;
        private final T result;
        private final boolean successful;

        RpcResultImpl( boolean successful, T result,
                       Collection<RpcError> errors ) {
            this.successful = successful;
            this.result = result;
            this.errors = errors;
        }

        @Override
        public Collection<RpcError> getErrors() {
            return errors;
        }

        @Override
        public T getResult() {
            return result;
        }

        @Override
        public boolean isSuccessful() {
            return successful;
        }

        @Override
        public String toString(){
            return "RpcResult [successful=" + successful + ", result="
                    + result + ", errors=" + errors + "]";
        }
    }

    private static class RpcErrorImpl implements RpcError {

        private final String applicationTag;
        private final String tag;
        private final String info;
        private final ErrorSeverity severity;
        private final String message;
        private final ErrorType errorType;
        private final Throwable cause;

        RpcErrorImpl( ErrorSeverity severity, ErrorType errorType,
                String tag, String message, String applicationTag, String info,
                Throwable cause ) {
            this.severity = severity;
            this.errorType = errorType;
            this.tag = tag;
            this.message = message;
            this.applicationTag = applicationTag;
            this.info = info;
            this.cause = cause;
        }

        @Override
        public String getApplicationTag() {
            return applicationTag;
        }

        @Override
        public String getTag() {
            return tag;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public ErrorSeverity getSeverity() {
            return severity;
        }

        @Override
        public String getMessage(){
            return message;
        }

        @Override
        public ErrorType getErrorType() {
            return errorType;
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public String toString(){
            return "RpcError [message=" + message + ", severity="
                    + severity + ", errorType=" + errorType + ", tag=" + tag
                    + ", applicationTag=" + applicationTag + ", info=" + info
                    + ", cause=" + cause + "]";
        }
    }

    private Collection<RpcError> errors;
    private T result;
    private final boolean successful;

    private RpcResultBuilder( boolean successful, T result ) {
        this.successful = successful;
        this.result = result;
    }

    /**
     * Returns a builder for a successful result.
     */
    public static <T> RpcResultBuilder<T> success() {
        return new RpcResultBuilder<T>( true, null );
    }

    /**
     * Returns a builder for a successful result.
     *
     * @param result the result value
     */
    public static <T> RpcResultBuilder<T> success( T result ) {
         return new RpcResultBuilder<T>( true, result );
    }

    /**
     * Returns a builder for a failed result.
     */
    public static <T> RpcResultBuilder<T> failed() {
        return new RpcResultBuilder<T>( false, null );
    }

    /**
     * Sets the value of the result.
     *
     * @param result the result value
     */
    public RpcResultBuilder<T> withResult( T result ) {
        this.result = result;
        return this;
    }

    private void addError( ErrorSeverity severity, ErrorType errorType,
            String tag, String message, String applicationTag, String info,
            Throwable cause ) {

        if( errors == null ) {
            errors = new ArrayList<>();
        }

        errors.add( new RpcErrorImpl( severity, errorType, tag, message,
                                      applicationTag, info, cause ) );
    }

    /**
     * Adds a warning to the result.
     *
     * @param errorType the conceptual layer at which the warning occurred.
     * @param tag a short string that identifies the general type of warning condition. See
     *        {@link RpcError#getTag} for a list of suggested values.
     * @param message a string suitable for human display that describes the warning condition.
     */
    public RpcResultBuilder<T> withWarning( ErrorType errorType, String tag, String message ) {
        addError( ErrorSeverity.WARNING, errorType, tag, message, null, null, null );
        return this;
    }

    /**
     * Adds a warning to the result.
     *
     * @param errorType the conceptual layer at which the warning occurred.
     * @param tag a short string that identifies the general type of warning condition. See
     *        {@link RpcError#getTag} for a list of suggested values.
     * @param message a string suitable for human display that describes the warning condition.
     * @param applicationTag a short string that identifies the specific type of warning condition.
     * @param info a string containing additional information to provide extended
     *        and/or implementation-specific debugging information.
     * @param cause the exception that triggered the warning.
     */
    public RpcResultBuilder<T> withWarning( ErrorType errorType, String tag, String message,
            String applicationTag, String info, Throwable cause ) {
        addError( ErrorSeverity.WARNING, errorType, tag, message, applicationTag, info, cause );
        return this;
    }

    /**
     * Adds an error to the result. The general error tag defaults to "operation-failed".
     *
     * @param errorType the conceptual layer at which the error occurred.
     * @param message a string suitable for human display that describes the error condition.
     */
    public RpcResultBuilder<T> withError( ErrorType errorType, String message ) {
        addError( ErrorSeverity.ERROR, errorType, "operation-failed", message, null, null, null );
        return this;
    }

    /**
     * Adds an error to the result.
     *
     * @param errorType the conceptual layer at which the error occurred.
     * @param tag a short string that identifies the general type of error condition. See
     *        {@link RpcError#getTag} for a list of suggested values.
     * @param message a string suitable for human display that describes the error condition.
     */
    public RpcResultBuilder<T> withError( ErrorType errorType, String tag, String message ) {
        addError( ErrorSeverity.ERROR, errorType, tag, message, null, null, null );
        return this;
    }

    /**
     * Adds an error to the result.
     *
     * @param errorType the conceptual layer at which the error occurred.
     * @param tag a short string that identifies the general type of error condition. See
     *        {@link RpcError#getTag} for a list of suggested values.
     * @param message a string suitable for human display that describes the error condition.
     * @param applicationTag a short string that identifies the specific type of error condition.
     * @param info a string containing additional information to provide extended
     *        and/or implementation-specific debugging information.
     * @param cause the exception that triggered the error.
     */
    public RpcResultBuilder<T> withError( ErrorType errorType, String tag, String message,
            String applicationTag, String info, Throwable cause ) {
        addError( ErrorSeverity.ERROR, errorType, tag, message, applicationTag, info, cause );
        return this;
    }

    public RpcResult<T> build() {

        return new RpcResultImpl<T>( successful, result,
                errors != null ? errors : Collections.<RpcError>emptyList() );
    }
}