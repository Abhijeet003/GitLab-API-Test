package com.abhijeet.org.GitLabAPITest.Controller;

import com.abhijeet.org.GitLabAPITest.Model.ModelDefinition;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("gitlab-api")
public class GitLabController {

    private static final String ACCESS_TOKEN = "fsSzJfBK27kQzhN1Jtrc";

//
//    @PostMapping("/createModel")
//    public void createModel(@RequestBody ModelDefinition modelDefinition) {
//            modelDefinition.setModelId(UUID.randomUUID().toString());
//            modelDefinition.setRepoName(gitLabService.getRepoNameForModel(modelDefinition));
//
//
//
//    }

    @PostMapping("/createModel")
    public GitlabProject createRepository (@RequestBody ModelDefinition modelDefinition) throws IOException {
        GitlabAPI api = GitlabAPI.connect("http://35.209.27.246", ACCESS_TOKEN);
        List<GitlabGroup> group = api.getGroups();

        for (GitlabGroup eachGroup : group) {
            if (eachGroup.getName().equalsIgnoreCase(modelDefinition.getDomain())) {
                List<GitlabGroup> subgroups = getSubGroups(eachGroup.getId());
                for (GitlabGroup eachSubGroup : subgroups) {
                    if(eachSubGroup.getName().equalsIgnoreCase(modelDefinition.getSubdomain())) {
                        GitlabProject project = api.createProjectForGroup(modelDefinition.getName(),eachSubGroup, null);
                        return project;
                    }
                }
            }
        }
        return null;
    }

    public List<GitlabGroup> getSubGroups(Integer groupId) {

            String url = "http://35.209.27.246/api/v4/groups/" + groupId + "/subgroups";

            RestTemplate restTemplate = new RestTemplate();

            ParameterizedTypeReference<List<GitlabGroup>> subgroups = new ParameterizedTypeReference<List<GitlabGroup>>() { };


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer "+ ACCESS_TOKEN);

            HttpEntity<String> request = new HttpEntity<>("parameters", headers);
           ResponseEntity<List<GitlabGroup>> response = restTemplate.exchange(url, HttpMethod.GET, request, subgroups);
           List<GitlabGroup> gitlabGroups = response.getBody();

            return gitlabGroups;
        }
//        if (group != null) {
//            //GitlabGroup gitlabGroup = api.createGroup(modelDefinition.getDomain());
//            GitlabGroup subgroup = api.createGroup(modelDefinition.getSubdomain(),modelDefinition.getSubdomain(),null,null,null,group.getId());
//            GitlabProject project = api.createProjectForGroup("models",subgroup);
//        }




}
