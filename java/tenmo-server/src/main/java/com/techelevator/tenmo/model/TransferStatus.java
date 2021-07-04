package com.techelevator.tenmo.model;

public class TransferStatus {
    private int transferId;
    private int transferStatus;

    public TransferStatus(int transferId, int transferStatus) {
        this.transferId = transferId;
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }
}
