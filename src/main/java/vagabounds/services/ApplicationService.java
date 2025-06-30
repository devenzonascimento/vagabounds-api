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



        //TODO VERIFCAR SE A VAGA ESTA ABERTA
        if (jobDTO.isOpen()) {

        }


        //TODO RETORNAR MENSAGEM DE ERRO, 'OOPS! THE JOB YOU ARE APPLYING IS ALREADY CLOSED'

        if(!jobDTO.isOpen()){
            throw new RuntimeException("OOPS! THE JOB YOU ARE APPLYING IS ALREADY CLOSED");
        }
    }


}
