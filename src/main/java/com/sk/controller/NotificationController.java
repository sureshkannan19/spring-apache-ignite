package com.sk.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sk.model.NotificationModel;
import com.sk.orchestrator.NotificationOrchestrator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class NotificationController {

	@Autowired
	private NotificationOrchestrator notifcationOrchestrator;

	@PostMapping(path = "/notification/updateTodaysFeed", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public List<NotificationModel> updateDetails(@RequestBody(required = true) List<NotificationModel> models)
			throws ParseException {
		log.info("Updating Notification...");
		return notifcationOrchestrator.updateAll(models);
	}

	@PostMapping(path = "/notification/updateDetailsViaMultipartFile", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public List<NotificationModel> updateDetailsViaMultipartFile(@RequestBody(required = true) MultipartFile file)
			throws ParseException, IOException {
		log.info("Updating Notification...");
		Reader fileReader = new InputStreamReader(new BOMInputStream(file.getInputStream()), StandardCharsets.UTF_8);
		List<NotificationModel> models = new ArrayList<>();
		try {
			Iterable<CSVRecord> csvRecords = CSVFormat.EXCEL.withHeader().parse(fileReader);
			for (CSVRecord record : csvRecords) {
				models.add(NotificationModel.builder().content(record.get("content")).headline(record.get("headline"))
						.build());
			}
		} finally {
			fileReader.close();
		}
		return notifcationOrchestrator.updateAll(models);
	}

	@GetMapping(path = "/notification/getFeedByContent", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<NotificationModel> getFeedByContent(@RequestParam(required = true, value = "keyword") String keyword) {
		log.info("Finding feed for keyword {} ...", keyword);
		return notifcationOrchestrator.findAllByContent(keyword);
	}

	@GetMapping(path = "/notification/getFeedById/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public NotificationModel getFeedById(@PathVariable(required = true, name = "id") Long id) {
		log.info("Finding feed for id {} ...", id);
		return notifcationOrchestrator.getFeedById(id);
	}
}
