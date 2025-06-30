package vagabounds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vagabounds.dtos.candidate.CandidateDTO;
import vagabounds.dtos.job.JobDTO;
import vagabounds.models.Application;
import vagabounds.repositories.ApplicationRepository;
import vagabounds.repositories.CandidateRepository;
import vagabounds.repositories.JobRepository;


@Service
public class ApplicationService {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    ApplicationRepository applicationRepository;


    public void applyToJob (CandidateDTO candidateDTO, JobDTO jobDTO) {

        //TODO VERIFICAR SE O USUARIO ESTA LOGADO
        if (){} throw new RuntimeException("You must be logged in to apply.");


        if(jobDTO.isOpen()){
            //TODO
        } throw new RuntimeException("OOPS! the job you are applying is already closed.");


        if(jobDTO.jobType().equals("INTERNSHIP") && candidateDTO.education().equals("UNDERGRAD")){
            //TODO
        } throw  new RuntimeException("Sorry, you must be a student to apply.");


    }


}
