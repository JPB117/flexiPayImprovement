package com.workpoint.icpak.shared.lms;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseDto {
	public String userName;
	public String courseName;

	public CourseDto() {
		// TODO Auto-generated constructor stub
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

}
