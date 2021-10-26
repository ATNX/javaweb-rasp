package org.javaweb.rasp.commons.cache;

import org.javaweb.rasp.commons.attack.RASPParameterPosition;

import java.util.Arrays;

public class RASPCachedParameter {

	private String key;

	private String[] value;

	private String[] lowerCaseValue;

	private RASPParameterPosition raspParameterPosition;

	public RASPCachedParameter(String key, String value, RASPParameterPosition raspParameterPosition) {
		this.key = key != null ? key : "";
		this.value = value != null ? new String[]{value} : new String[0];
		this.raspParameterPosition = raspParameterPosition;
	}

	public RASPCachedParameter(String key, String[] value, RASPParameterPosition raspParameterPosition) {
		this.key = key != null ? key : "";
		this.value = value != null ? value : new String[0];
		this.raspParameterPosition = raspParameterPosition;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String[] getValue() {
		return value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

	public String[] getLowerCaseValue() {
		if (lowerCaseValue != null)
			return lowerCaseValue;

		lowerCaseValue = new String[value.length];

		for (int i = 0; i < value.length; i++) {
			String val = value[i];
			lowerCaseValue[i] = val != null ? val.toLowerCase() : null;
		}

		return lowerCaseValue;
	}

	public RASPParameterPosition getRaspAttackPosition() {
		return raspParameterPosition;
	}

	public void setRaspAttackPosition(RASPParameterPosition raspParameterPosition) {
		this.raspParameterPosition = raspParameterPosition;
	}

	public boolean containsValue(String value) {
		if (this.value != null) {
			for (String vul : this.value) {
				return vul != null && vul.equals(value);
			}
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RASPCachedParameter that = (RASPCachedParameter) o;

		if (!key.equals(that.key)) return false;
		if (!Arrays.equals(value, that.value)) return false;
		return raspParameterPosition == that.raspParameterPosition;
	}

	@Override
	public int hashCode() {
		int result = key != null ? key.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(value);
		result = 31 * result + (raspParameterPosition != null ? raspParameterPosition.hashCode() : 0);
		return result;
	}

}
