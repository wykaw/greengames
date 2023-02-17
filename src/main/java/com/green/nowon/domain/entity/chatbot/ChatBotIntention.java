package com.green.nowon.domain.entity.chatbot;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "intention")
@Entity
public class ChatBotIntention {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long no;
	private String name;
	
	@JoinColumn
	@ManyToOne
	private Answer answer;
		
	@JoinColumn
	@ManyToOne
	private ChatBotIntention upper;
}
