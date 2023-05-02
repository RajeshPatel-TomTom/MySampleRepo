/*
 * Copyright (C), the copyright owner 'TomTom', 2022.
 */
package com.tomtom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author giridhar
 */
@Builder(setterPrefix = "with", toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PBFInfo {
	private int version;
	private Date timestamp;
	private long changeset;
	private int userId;
	private String userName;
}
