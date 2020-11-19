package com.blockwit.bwf.exception;

import com.blockwit.bwf.model.ConfirmationStatus;

public class WrongConfirmStatusAccountServiceException extends AccountServiceException {

	ConfirmationStatus actualConfirmStatus;

	ConfirmationStatus expectedConfirmStatus;

	public WrongConfirmStatusAccountServiceException(ConfirmationStatus actualConfirmStatus, ConfirmationStatus expectedConfirmStatus) {
		super("Expected confirm status " + expectedConfirmStatus.name() + " when actual " + actualConfirmStatus.name());
		this.actualConfirmStatus = actualConfirmStatus;
		this.expectedConfirmStatus = expectedConfirmStatus;
	}

}
