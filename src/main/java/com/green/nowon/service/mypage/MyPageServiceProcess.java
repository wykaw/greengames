package com.green.nowon.service.mypage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.green.nowon.domain.dto.memberDTO.AddressDetailDTO;
import com.green.nowon.domain.dto.memberDTO.DepartmentDTO;
import com.green.nowon.domain.dto.memberDTO.DepartmentMemberDTO;
import com.green.nowon.domain.dto.memberDTO.MemberDetailDTO;
import com.green.nowon.domain.dto.memberDTO.ProfileDTO;
import com.green.nowon.domain.dto.memberDTO.SalaryListDTO;
import com.green.nowon.domain.dto.position.PositionDTO;
import com.green.nowon.domain.entity.cate.DepartmentEntity;
import com.green.nowon.domain.entity.cate.DepartmentEntityRepository;
import com.green.nowon.domain.entity.cate.DepartmentMemberEntity;
import com.green.nowon.domain.entity.cate.DepartmentMemberEntityRepository;
import com.green.nowon.domain.entity.cate.PositionEntity;
import com.green.nowon.domain.entity.cate.PositionRepository;
import com.green.nowon.domain.entity.member.AddressEntity;
import com.green.nowon.domain.entity.member.AddressEntityRepsoitory;
import com.green.nowon.domain.entity.member.MemberEntity;
import com.green.nowon.domain.entity.member.MemberEntityRepository;
import com.green.nowon.domain.entity.member.ProfileEntity;
import com.green.nowon.domain.entity.member.ProfileEntityRepository;
import com.green.nowon.security.MyRole;

@Service
public class MyPageServiceProcess implements MyPageService{

	
	@Autowired
	private MemberEntityRepository mRepo;
	@Autowired
	private AddressEntityRepsoitory aRepo;
	@Autowired
	private ProfileEntityRepository proRepo;
	@Autowired
	private DepartmentEntityRepository dRepo;
	@Autowired
	private PositionRepository pRepo;
	@Autowired
	private DepartmentMemberEntityRepository dmRepo;

	
	
	@Transactional
	@Override
	public void info(long mno, Model model) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>mno= "+mno);
		
		 //???????????? ????????? ?????? + ????????? ????????? ?????????
		model.addAttribute("detail",mRepo.findById(mno)
				.map(SalaryListDTO::new)
				.orElseThrow());
		//???????????? ????????? ????????????
		model.addAttribute("mpageaddr",aRepo.findByMember_mno(mno)
				.map(AddressDetailDTO::new)
				.orElseThrow());
	}

	
	@Transactional
	@Override//????????????????????? ????????????
	public void list(long mno, Model model) {
		List<SalaryListDTO> result=mRepo.findAll().stream()
				.map(SalaryListDTO::new).collect(Collectors.toList());
		model.addAttribute("srlist", result);
	}
	
	@Transactional
	@Override //??????????????????????????? ????????? ??????????????????
	public void salaryInfo(long mno, Model model) {
		
//		System.out.println("start : findByMember_mnoAndDepartment_depth");
		Optional<DepartmentMemberEntity> ee = dmRepo.findByMember_mnoAndDepartment_depth(mno,3);
//		System.out.println("end : findByMember_mnoAndDepartment_depth");
	
//		System.out.println(ee.get().getDepartment());
//		System.out.println(ee.get().getMember().getId());
//		System.out.println(ee.get().getMember().getMno());
//		System.out.println(ee.get().getMember().getProfile().getUrl());
//		
//		List<ProfileEntity> pfile = ee.get().getMember().getProfile();
//		System.out.println(pfile.size());
//		for(ProfileEntity p : pfile) {
//			System.out.println(p.getNewName());	
//		}
//		Set<MyRole> set =ee.get().getMember().getRoles();
//		for(MyRole r : set) {
//			System.out.println(r.getRole());
//		}
//		
//		model.addAttribute("detail",
//				dmRepo.findByMember_mnoAndDepartment_depth(mno,3)
//				.map(DepartmentMemberDTO::new)
//				.orElseThrow());

		
		model.addAttribute("detail",mRepo.findById(mno)
				.map(SalaryListDTO::new).orElseThrow());
		
		
		List<DepartmentDTO> result=dRepo.findAllByDepth(3).stream()
				.map(DepartmentDTO::new).collect(Collectors.toList());
		model.addAttribute("department",result);
		
				
		List<PositionDTO> aaa=pRepo.findAll().stream()
				.map(PositionDTO::new).collect(Collectors.toList());
		model.addAttribute("position",aaa);	
	}
	
	
	@Override
	public void update(long mno, long pno,long dno) {
		Optional<MemberEntity> me=mRepo.findById(mno);
		
		me.ifPresent(e -> { //????????????
			e.setPno(PositionEntity.builder()
					.pno(pno)
					.build());
			mRepo.save(e);
		});
		
		//???????????? ?????? ??????
		List<DepartmentMemberEntity> temp = dmRepo.findAllByMember_mno(mno);
		for(DepartmentMemberEntity d : temp ) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>"+temp);
		}
		dmRepo.deleteByMember_mno(mno);
		dmRepo.save(DepartmentMemberEntity.builder() //????????????
				.department(DepartmentEntity.builder() .dno(dno) .build())
				.member(MemberEntity.builder() .mno(mno) .build()) .build());
	}

	@Transactional
	@Override
	public void update2(long mno, double plSal, Integer totSal) {
		Optional<MemberEntity> mem=mRepo.findById(mno);
		mem.ifPresent(e->{
			e.setBoList(plSal);
			e.setTotSalary(totSal);
		mRepo.save(e);
		});
		
	}












	


		
	

}
