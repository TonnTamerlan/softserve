/**
 * 
 */
package com.softserveinc.test_task.dao;

/**
 * @author Alexey Kopylov
 *
 */
public class DAOException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * 
     * Constructs a new data base exception with the specified detail message and cause. Note
     * that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     * 
     * @param message - the detail message (which is saved for later retrieval by the
     * Throwable.getMessage() method).
     * @param cause - the cause (which is saved for later
     * retrieval by the Throwable.getCause() method). (A null value is permitted,
     * and indicates that the cause is nonexistent or unknown.)
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
