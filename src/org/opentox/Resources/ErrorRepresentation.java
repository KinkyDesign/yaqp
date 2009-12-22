package org.opentox.Resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.StreamRepresentation;

/**
 * An error representation is the web representation of an exception that is
 * thrown by a method during following some HTTP request.
 * @author OpenTox - http://www.opentox.org/
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 * @version 1.3.3 (Last update: Dec 20, 2009)
 */
public class ErrorRepresentation extends StreamRepresentation {


    /**
     * Maps a Throwable to a corresponding explanation.
     */

    private Map<String, Throwable> map = new HashMap<String, Throwable>();


    /**
     * Internal Status of the error representation.
     */
    private volatile Status internalStatus = Status.SUCCESS_ACCEPTED;



    public ErrorRepresentation() {
        super(MediaType.TEXT_PLAIN);
    }

    
    public ErrorRepresentation(
            Map<String, Throwable> map,
            Status status) {
        this();
        this.map = map;
        updateStatus(status);
    }

    public ErrorRepresentation(
            Throwable throwable,
            String message,
            Status status)
    {
        this();
        this.map.put(message, throwable);
        updateStatus(status);
    }

    
    /**
     * Concatenates two Error Representations by mixing together their explanatory
     * messages and their lists of Throwables.
     * @param other
     * @return
     */
    public ErrorRepresentation append(ErrorRepresentation other) {
        ErrorRepresentation rep = this;
        this.map.putAll(other.map);
        this.updateStatus(other.getStatus());
        return this;
    }


    public ErrorRepresentation append(Throwable throwable, String message, Status status){
        this.append(new ErrorRepresentation(throwable, message, status));
        return this;
    }

    /**
     * The Error Level is defined to be the number of Error or exception in this
     * object.
     */
    public int errorLevel(){
        return map.size();
    }
    

    /**
     * The explanatory messages about the errors.
     * @return
     */
    public Set<String> getMessages() {
        return (Set<String>)map.keySet();
    }


    /**
     * Updates the (internal) status.The status can be
     * updated only in the following cases:
     * <ul>
     * <li>The current status is less than 300, i.e. No errors occured up to the current state</li>
     * <li>The current status is 4XX and the new status is also
     * a 4XX or a 5XX status (client or server error).</li>
     * <li>If the current status is a 5XX (eg 500), it will not be updated</li>
     * </ul>
     * @param newStatus
     * @return
     */
    public ErrorRepresentation updateStatus(Status newStatus){
        if (
                (this.internalStatus.getCode()<300) ||
                ((newStatus.getCode()>=400)&&(newStatus.getCode()<500)&&internalStatus.getCode()>=400)
                )
           {
            this.internalStatus = newStatus;
        }
        return this;
    }

    public Status getStatus(){
        return this.internalStatus;
    }

    /**
     * Returns the representation as plain text.
     * @return
     */
    @Override
    public String getText() {

        if (MediaType.TEXT_PLAIN.equals(getMediaType())) {

            String outputMessage = "Error Report.\n" +
                    "TimeStamp: " + GregorianCalendar.getInstance().getTime()+"\n\n";
            int i=0;
                      
            for (Map.Entry<String, Throwable> e : map.entrySet()){
                outputMessage = outputMessage + ("Error #"+(++i)+"\n");
                outputMessage = outputMessage + ("Exception Details: "+e.getValue()+"\n");
                outputMessage = outputMessage + ("Explanation: "+e.getKey()+"\n");
                outputMessage = outputMessage + ("For debugging reasons we provide a brief list of the exceptions: \n");
                StackTraceElement[] ste = e.getValue().getStackTrace();
                for (int j=0;j<4;j++){
                    outputMessage = outputMessage + "- " +ste[j].toString()+"\n";
                }
                outputMessage = outputMessage + "\n\n";
            }
            return outputMessage;
        }

        return null;
    }

    /**
     * Returns a stream with the content of the representation.
     * @return A stream with the representation's content.
     * @throws IOException
     * @see StreamRepresentation#getStream()
     */
    @Override
    public InputStream getStream() throws IOException {
        if (getText() != null) {
            if (getCharacterSet() != null) {
                return new ByteArrayInputStream(getText().getBytes(
                        getCharacterSet().getName()));
            }

            return new ByteArrayInputStream(getText().getBytes());
        }

        return null;
    }

    /**
     * Writes the representation to an output stream.
     * @param outputStream
     * @throws IOException
     * @see StreamRepresentation#write(java.nio.channels.WritableByteChannel)
     * @see StreamRepresentation#write(java.io.Writer)
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (getText() != null) {
            OutputStreamWriter osw = null;

            if (getCharacterSet() != null) {
                osw = new OutputStreamWriter(outputStream, getCharacterSet().getName());
            } else {
                osw = new OutputStreamWriter(outputStream);
            }

            osw.write(getText());
            osw.flush();
        }
    }


}


