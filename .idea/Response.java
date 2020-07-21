public class Response{

    @JsonProperty("petId")
    private int petId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("id")
    private int id;

    @JsonProperty("shipDate")
    private String shipDate;

    @JsonProperty("complete")
    private boolean complete;

    @JsonProperty("status")
    private String status;

    public int getPetId(){
        return petId;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getId(){
        return id;
    }

    public String getShipDate(){
        return shipDate;
    }

    public boolean isComplete(){
        return complete;
    }

    public String getStatus(){
        return status;
    }
}
