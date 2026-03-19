package kr.co.wizbrain.tbn.comm.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.AbstractView;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;
/* loaded from: ExportPoiHssfExcel.class */
public class ExportPoiHssfExcel extends AbstractView {
    private static final Log log = LogFactory.getLog(ExportPoiHssfExcel.class);
    String filename;
    FileOutputStream fileout;

    public ExportPoiHssfExcel() {
        super.setContentType("application/octet-stream");
    }

    private String getFileName(Map<?, ?> model) {
        String name = (String) model.get("fileName");
        try {
            return URLEncoder.encode(name, "UTF-8").replace('+', ' ');
        } catch (UnsupportedEncodingException e) {
            return name;
        }
    }

    private List<String> getColumnTitles(Map<?, ?> model) {
        String[] titles = (String[]) model.get("columnTitles");
        List<String> list = new ArrayList<>();
        for (String str : titles) {
            list.add(str);
        }
        return list;
    }

    private List<List<String>> getExportthisMonth(Map<?, ?> model) {
        String[] names = (String[]) model.get("columnNames");
        List<?> thisMonth = (List) model.get("exportthisMonth");
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < thisMonth.size(); i++) {
            RecordDto record = (RecordDto) thisMonth.get(i);
            List<String> item = new ArrayList<>();
            for (String str : names) {
                item.add(record.getString(str));
            }
            list.add(item);
        }
        return list;
    }

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            response.setHeader("Cache-Control", "no-cache");
            setContentType("application/vnd.ms-excel;charset=UTF-8");
            HSSFWorkbook wb = new HSSFWorkbook();
            this.filename = getFileName(model);
            if (model.get("mapping").equals("standardInformerType")) {
                standardInformerType(model, wb);
            } else if (model.get("mapping").equals("extrBro")) {
                extrBro(model, wb);
            } else if (model.get("mapping").equals("disastorStat")) {
                disastorStat(model, wb);
            } else if (model.get("mapping").equals("muJebo")) {
                muJebo(model, wb);
            } else if (model.get("mapping").equals("muJebo2")) {
                muJebo2(model, wb);
            } else if (model.get("mapping").equals("standardInformUse")) {
                standardInformUse(model, wb);
            } else if (model.get("mapping").equals("standartdReceiptUse")) {
                standartdReceiptUse(model, wb);
            } else if (model.get("mapping").equals("nationalIncident")) {
                nationalIncident(model, wb);
            } else if (model.get("mapping").equals("standardInformer")) {
                standardInformer(model, wb);
            } else if (model.get("mapping").equals("krGas")) {
                korLx(model, wb);
            } else if (model.get("mapping").equals("korLx")) {
                korLx(model, wb);
            } else if (model.get("mapping").equals("receiptDown")) {
                receiptDown(model, wb);
            } else if (model.get("mapping").equals("dayReceipt")) {
                dayReceipt(model, wb);
            } else if (model.get("mapping").equals("orgOrgSub")) {
            	orgOrgSub(model, wb);
            } else if (model.get("mapping").equals("volunteer")) {
                volunteer(model, wb);
            } else if (model.get("mapping").equals("informerDown")) {
                informerDown(model, wb);
            } else if (model.get("mapping").equals("informerReport")) {
            	informerReport(model, wb);
            } else if(model.get("mapping").equals("addDownload")) {
            	addDownload(model, wb);
            } else if (model.get("mapping").equals("standardInformerTypeTime")) {
            	standardInformerTypeTime(model, wb);
            } else if (model.get("mapping").equals("yearReceipt")) {
            	yearReceipt(model, wb);
            } else if (model.get("mapping").equals("yearOrgStat")) {
            	yearOrgStat(model, wb);
            } else if (model.get("mapping").equals("mileageExcel") || model.get("mapping").equals("excellenceExcel")) {
            	mileageExcel(model, wb);
            }
            
            ServletOutputStream outputStream = response.getOutputStream();
            //response.setContentType(super.getContentType());
            
            response.setCharacterEncoding("UTF-8"); 
            response.setContentType("application/vnd.ms-excel");
            
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + this.filename + "\";");
            
            
            
            wb.write(outputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    protected void standardInformerType(Map model, HSSFWorkbook wb) {
        List titleMain = (List) model.get("titleMain");
        List headMain = (List) model.get("headMain");
        List sumMain = (List) model.get("sumMain");
        List sumOurOtherMain = (List) model.get("sumOurOtherMain");
        List dataMain = (List) model.get("dataMain");
        for (int i = 0; i < titleMain.size(); i++) {
            informerType(wb, (String) titleMain.get(i), (List) headMain.get(i), (List) sumMain.get(i), (List) sumOurOtherMain.get(i), (List) dataMain.get(i));
        }
    }

	protected void informerType(HSSFWorkbook wb, String titleData, List headData, List headData1, List headData2,
			List data) {
		int rowCnt = 0;
		HSSFSheet sheet1 = wb.createSheet(titleData);
		String csType = "";
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    int titleSize = (headData.size() * 2) + 3; // 제목 병합용 변수
 	    
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, titleSize)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
			
        HSSFCell titleCell = titlerow.createCell(rowCnt);
        titleCell.setCellValue("교통정보 제공대장");
        titleCell.setCellStyle(titleStyle);
        
		titlerow.setHeight((short) 800);
		rowCnt++;
		
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
		headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font headStylefont = wb.createFont(); // 폰트 객체 생성
 	    headStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    headStyle.setFont(headStylefont); // 폰트 스타일을 셀 스타일에 적용
		
		HSSFRow headrow = sheet1.createRow(rowCnt);
		
		HSSFCell headTitle = headrow.createCell(0);	
		headTitle.setCellValue(titleData);
		headTitle.setCellStyle(headStyle);
		headrow.createCell(1).setCellStyle(headStyle);	
		sheet1.setColumnWidth(0, 4000);
		sheet1.setColumnWidth(1, 4000);

/*		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));*/
		
		HSSFCell sumTitle = headrow.createCell(2);	
		sumTitle.setCellValue("계");
		sumTitle.setCellStyle(headStyle);
		headrow.createCell(3).setCellStyle(headStyle);
		
		for (int j = 0; j < headData.size(); ++j) {
			RecordDto record = (RecordDto) headData.get(j);
			
			HSSFCell headCell = headrow.createCell(j * 2 + 4);
			HSSFCell headCell2 = headrow.createCell(j * 2 + 5);
			headCell.setCellValue(record.getString("CODE_NAME"));
			headCell.setCellStyle(headStyle);
			headCell2.setCellStyle(headStyle);
		}

		DataFormat format = wb.createDataFormat();
		
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
		
		
		
		rowCnt++;
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		int sum = 0;

		for (int j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			
			HSSFCell headsumCell = headrow1.createCell(j * 2 + 4);
			HSSFCell headsumCell2 = headrow1.createCell(j * 2 + 5);
			
			headsumCell.setCellValue((double) record.getInt("CNT"));
			headsumCell.setCellStyle(dataStyle);
			headsumCell2.setCellStyle(dataStyle);
			 
			sum += record.getInt("CNT");
		}

		HSSFCell suCell = headrow1.createCell(0);
		suCell.setCellValue("수집건수");
		suCell.setCellStyle(dataStyle);
		
		HSSFCell sumCell = headrow1.createCell(2);
		sumCell.setCellValue((double) sum);
		sumCell.setCellStyle(dataStyle);

		for (int j = 0; j < headData.size() + 2; ++j) {
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, j * 2, j * 2 + 1));
			if (j == 0) {
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt + 2, j * 2, j * 2 + 1));
			} else {
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, j * 2, j * 2 + 1));
			}
		}

		rowCnt++;
		headrow1 = sheet1.createRow(rowCnt);

		for (int j = 0; j < headData.size() + 1; ++j) {
			
			HSSFCell ourheadCell = headrow1.createCell(j * 2 + 2);
			HSSFCell otherheadCell = headrow1.createCell(j * 2 + 3);
			
			ourheadCell.setCellValue("자국");
			otherheadCell.setCellValue("타국");
			
			ourheadCell.setCellStyle(headStyle);
			otherheadCell.setCellStyle(headStyle);
			
			sheet1.setColumnWidth(j * 2 + 2, 4000);
			sheet1.setColumnWidth(j * 2 + 3, 4000);
		}

		rowCnt++;
		headrow1 = sheet1.createRow(rowCnt);
		int sumOur = 0;
		int sumOther = 0;

		for (int j = 0; j < headData2.size(); ++j) {
			RecordDto record = (RecordDto) headData2.get(j);
			
			HSSFCell ourothCell = headrow1.createCell(j + 4);
			ourothCell.setCellValue((double) record.getInt("CNT"));
			ourothCell.setCellStyle(dataStyle);
			
			if (record.getString("REGION_ID").equals("000")) {
				sumOther += record.getInt("CNT");
			} else {
				sumOur += record.getInt("CNT");
			}
		}

		HSSFCell oursumCell = headrow1.createCell(2);
		HSSFCell othersumCell = headrow1.createCell(3);
		
		oursumCell.setCellValue((double) sumOur);
		othersumCell.setCellValue((double) sumOther);
		
		oursumCell.setCellStyle(dataStyle);
		othersumCell.setCellStyle(dataStyle);
		
		rowCnt++;
		DecimalFormat df = new DecimalFormat("##0.0%");

		for (int i = 0; i < data.size() / (headData.size() * 2); ++i) {
			HSSFRow dataRow = sheet1.createRow(rowCnt++);
			HSSFRow ratioRow = sheet1.createRow(rowCnt++);
			sumOur = 0;
			sumOther = 0;

			for (int j = 0; j < headData.size() * 2; ++j) {
				RecordDto record = (RecordDto) data.get(i * headData.size() * 2 + j);
				if (j == 0) {
					String TYPE = record.getString("TYPE");
					if (!TYPE.contains("CASTER") && !TYPE.contains("STUDIO")) {
						if (!TYPE.equals("전송건수") && !TYPE.equals("방송건수")) {
							HSSFCell typeCell = dataRow.createCell(0);
							HSSFCell sendCell = dataRow.createCell(1);
							HSSFCell perCell = ratioRow.createCell(1);

							typeCell.setCellValue(TYPE);
							sendCell.setCellValue("건수");
							perCell.setCellValue("비율");
							
							typeCell.setCellStyle(dataStyle);
							sendCell.setCellStyle(dataStyle);
							perCell.setCellStyle(dataStyle);
						} else {
							if (TYPE.equals("전송건수")) {
								HSSFCell impsendCell = dataRow.createCell(0);
								impsendCell.setCellValue("중요제보");
								impsendCell.setCellStyle(dataStyle);
							} else {
								HSSFCell brodperCell = ratioRow.createCell(1);
								brodperCell.setCellValue("방송비율");
								brodperCell.setCellStyle(dataStyle);
							}
							HSSFCell typeCell = dataRow.createCell(1);
							typeCell.setCellValue(TYPE);
							typeCell.setCellStyle(dataStyle);
						}
					} else {
						csType = TYPE.split("_")[0];
						TYPE = TYPE.split("_")[1];
						
						HSSFCell typeCell = dataRow.createCell(1);
						typeCell.setCellValue(TYPE);
						typeCell.setCellStyle(dataStyle);
					}
				}

				if (dataRow.getCell(0) != null && !dataRow.getCell(0).getStringCellValue().equals("방송")
						&& !dataRow.getCell(0).getStringCellValue().equals("비방송")) {
					dataRow.getCell(0).getStringCellValue().equals("중요제보");
				}

				HSSFCell dataCell = dataRow.createCell(j + 4);
				dataCell.setCellValue((double) record.getInt("CNT"));
				dataCell.setCellStyle(dataStyle);

				if (!dataRow.getCell(1).getStringCellValue().equals("STUDIO")
						&& !dataRow.getCell(1).getStringCellValue().equals("CASTER")
						&& !dataRow.getCell(1).getStringCellValue().equals("전송건수")) {
					if (dataRow.getCell(0) != null) {
						if (!dataRow.getCell(0).getStringCellValue().equals("방송")
								&& !dataRow.getCell(0).getStringCellValue().equals("비방송")) {
							if (headrow1.getCell(j + 4).getNumericCellValue() == 0.0) {
								HSSFCell ratioCell = ratioRow.createCell(j + 4);
								ratioCell.setCellValue("0.0%");
								ratioCell.setCellStyle(dataStyle);
							} else {
								HSSFCell ratioCell = ratioRow.createCell(j + 4);
								ratioCell.setCellValue(df.format(
										record.getDouble("CNT") / headrow1.getCell(j + 4).getNumericCellValue()));
								ratioCell.setCellStyle(dataStyle);
							}
						} else if (headrow1.getCell(j + 4).getNumericCellValue() == 0.0) {			
							HSSFCell ratioCell = ratioRow.createCell(j + 4);
							ratioCell.setCellValue("0.0%");
							ratioCell.setCellStyle(dataStyle);
						} else {
							HSSFCell ratioCell = ratioRow.createCell(j + 4);
							ratioCell.setCellValue(df.format(
									record.getDouble("CNT") / sheet1.getRow(7).getCell(j + 4).getNumericCellValue()));
							ratioCell.setCellStyle(dataStyle);
						}
					} else if (headrow1.getCell(j + 4).getNumericCellValue() == 0.0) {
						HSSFCell ratioCell = ratioRow.createCell(j + 4);
						ratioCell.setCellValue("0.0%");
						ratioCell.setCellStyle(dataStyle);
					} else {
						HSSFCell ratioCell = ratioRow.createCell(j + 4);
						ratioCell.setCellValue(df.format(
								record.getDouble("CNT") / sheet1.getRow(15).getCell(j + 4).getNumericCellValue()));
						ratioCell.setCellStyle(dataStyle);
					}
				}

				if (record.getString("REGION_ID").equals("000")) {
					sumOther += record.getInt("CNT");
				} else {
					sumOur += record.getInt("CNT");
				}
			}


			HSSFCell oursumCell2 = dataRow.createCell(2);
			oursumCell2.setCellValue((double) sumOur);
			oursumCell2.setCellStyle(dataStyle);
			
			HSSFCell oursumCell3 = dataRow.createCell(3);
			oursumCell3.setCellValue((double) sumOther);
			oursumCell3.setCellStyle(dataStyle);
			
			
			if (!dataRow.getCell(1).getStringCellValue().equals("STUDIO")
					&& !dataRow.getCell(1).getStringCellValue().equals("CASTER")
					&& !dataRow.getCell(1).getStringCellValue().equals("전송건수")) {
				if (dataRow.getCell(0) != null) {
					if (!dataRow.getCell(0).getStringCellValue().equals("방송")
							&& !dataRow.getCell(0).getStringCellValue().equals("비방송")) {
						if (headrow1.getCell(2).getNumericCellValue() == 0.0) {
							
							HSSFCell ratioCell = ratioRow.createCell(2);
							ratioCell.setCellValue("0.0%");
							ratioCell.setCellStyle(dataStyle); // (계 - 자국)
						} else {
							HSSFCell ratioCell = ratioRow.createCell(2);
							ratioCell.setCellValue(
									df.format((double) sumOur / headrow1.getCell(2).getNumericCellValue()));
							ratioCell.setCellStyle(dataStyle); // (계 - 자국)
						}

						if (headrow1.getCell(3).getNumericCellValue() == 0.0) {
							HSSFCell ratioCell = ratioRow.createCell(3);
							ratioCell.setCellValue("0.0%");
							ratioCell.setCellStyle(dataStyle); // (계 - 타국)
						} else {
							
							HSSFCell ratioCell = ratioRow.createCell(3);
							ratioCell.setCellValue(
									df.format((double) sumOther / headrow1.getCell(3).getNumericCellValue()));
							ratioCell.setCellStyle(dataStyle); // (계 - 타국)
						}
					} else {
						if (headrow1.getCell(2).getNumericCellValue() == 0.0) {
							HSSFCell ratioCell = ratioRow.createCell(2);
							ratioCell.setCellValue("0.0%");
							ratioCell.setCellStyle(dataStyle); // (계 - 자국)
						} else {
							HSSFCell ratioCell = ratioRow.createCell(2);
							ratioCell.setCellValue(
									df.format((double) sumOur / sheet1.getRow(7).getCell(2).getNumericCellValue()));
							ratioCell.setCellStyle(dataStyle); // (계 - 자국)
						}

						if (headrow1.getCell(3).getNumericCellValue() == 0.0) {
							HSSFCell ratioCell = ratioRow.createCell(3);
							ratioCell.setCellValue("0.0%");
							ratioCell.setCellStyle(dataStyle); // (계 - 타국)
						} else {
							HSSFCell ratioCell = ratioRow.createCell(3);
							ratioCell.setCellValue(
									df.format((double) sumOther / sheet1.getRow(7).getCell(3).getNumericCellValue()));
							ratioCell.setCellStyle(dataStyle); // (계 - 타국)
						
						}
					}
				} else {
					if (headrow1.getCell(2).getNumericCellValue() == 0.0) {
						HSSFCell ratioCell = ratioRow.createCell(2);
						ratioCell.setCellValue("0.0%");
						ratioCell.setCellStyle(dataStyle); // (계 - 자국)
					} else {
						HSSFCell ratioCell = ratioRow.createCell(2);
						ratioCell.setCellValue(
								df.format((double) sumOur / sheet1.getRow(15).getCell(2).getNumericCellValue()));
						ratioCell.setCellStyle(dataStyle); // (계 - 자국)
					}

					if (headrow1.getCell(3).getNumericCellValue() == 0.0) {
						HSSFCell ratioCell = ratioRow.createCell(3);
						ratioCell.setCellValue("0.0%");
						ratioCell.setCellStyle(dataStyle); // (계 - 타국)
					} else {
						HSSFCell ratioCell = ratioRow.createCell(3);
						ratioCell.setCellValue(
								df.format((double) sumOther / sheet1.getRow(15).getCell(3).getNumericCellValue()));
						ratioCell.setCellStyle(dataStyle); // (계 - 타국)
					}
				}
			} else {
				rowCnt--;
			}

			log.debug("★★★ " + (rowCnt - 1));
			log.debug("★★★ " + dataRow.getCell(1).getStringCellValue());
			if (csType.equals("방송") && dataRow.getCell(1).getStringCellValue().equals("CASTER")) {
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 4, rowCnt - 1, 0, 0));
			} else if (csType.equals("중요제보") && dataRow.getCell(1).getStringCellValue().equals("CASTER")) {
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 5, rowCnt - 1, 0, 0));
			} else if (dataRow.getCell(0) != null && !dataRow.getCell(0).getStringCellValue().equals("방송")
					&& !dataRow.getCell(1).getStringCellValue().equals("STUDIO")
					&& !dataRow.getCell(1).getStringCellValue().equals("방송건수")
					&& !dataRow.getCell(1).getStringCellValue().equals("전송건수")) {
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 2, rowCnt - 1, 0, 0));
			}
		}

		
		
		
		
	}
    
	
	// 긴급교통정보 방송 현황 분석
    protected void extrBro(Map model, HSSFWorkbook wb) {
    	
    	// 기본 변수 생성
 		int i = 0;
		int j = 0;
		int rowCnt = 0;
		
		// 엑셀 시트 생성
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
		// 타이틀 셀에 삽입 => 긴급교통정보 처리건수 실적 (1행)
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		HSSFCell titleCell = titlerow.createCell(rowCnt);	
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // 행 병합
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		
		sheet1.setColumnWidth(0, 8000);
		sheet1.setColumnWidth(1, 8000);
		sheet1.setColumnWidth(2, 8000);

		rowCnt++;
		rowCnt++;
		rowCnt++;
		
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
		headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font headStylefont = wb.createFont(); // 폰트 객체 생성
 	    headStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    headStyle.setFont(headStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    HSSFCell dayCell = headrow1.createCell(0);
	 	HSSFCell allImpCell = headrow1.createCell(1);
	 	HSSFCell brod5Cell = headrow1.createCell(2);
	 	
		// 일자, 총 긴급정보 건수, 5분내 방송처리 건수 (4행)
	 	dayCell.setCellValue("일자");
	 	allImpCell.setCellValue("총 긴급정보 건수");
	 	brod5Cell.setCellValue("5분내 방송처리 건수");
	 	
	 	dayCell.setCellStyle(headStyle);
	 	allImpCell.setCellStyle(headStyle);
	 	brod5Cell.setCellStyle(headStyle);
		
		rowCnt++;
		HSSFRow headrow2 = sheet1.createRow(rowCnt);
		
		int sum1 = 0;
		int sum2 = 0;
		
		// 시작일 ~ 종료일 까지 데이터 가져오기 (cnt 등)
		List data = (List) model.get("Data");
		
		rowCnt++;
		Calendar ca = Calendar.getInstance();

		// 시작일, 종료일 가져오기
		String Sdate = (String) model.get("start_date");
		String Edate = (String) model.get("end_date");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		LocalDate startDate = LocalDate.parse(Sdate, formatter);
		LocalDate endDate = LocalDate.parse(Edate, formatter);
		
		int maxMon = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

	    List<String> dateList = new ArrayList<>(maxMon);

	    LocalDate currentDate = startDate;
	    while (!currentDate.isAfter(endDate)) {
	    	dateList.add(currentDate.format(formatter)); 
	        currentDate = currentDate.plusDays(1); 
	    }

	    DataFormat format = wb.createDataFormat();

	    CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    dataStyle.setDataFormat(format.getFormat("#,##0"));
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
	    HSSFRow[] dataRow = new HSSFRow[maxMon];
			
		for (i = 0; i < maxMon; ++i) {
			dataRow[i] = sheet1.createRow(rowCnt + i);
			String inputDate = "";
			
			HSSFCell dateCell = dataRow[i].createCell(0);
			dateCell.setCellValue(dateList.get(i));
			dateCell.setCellStyle(dataStyle);
			
			inputDate = dateList.get(i);
			
			for (int k = 0; k < data.size(); ++k) {
				RecordDto record = (RecordDto) data.get(k);
				
				if (inputDate.equals(record.getString("KEY_DATE"))) {
					
					HSSFCell dataCell = dataRow[i].createCell(1);
					HSSFCell dataCell2 = dataRow[i].createCell(2);
					dataCell.setCellValue((double) record.getInt("CNT1"));
					dataCell2.setCellValue((double) record.getInt("CNT2"));
					dataCell.setCellStyle(dataStyle);
					dataCell2.setCellStyle(dataStyle);

					sum1 += record.getInt("CNT1");
					sum2 += record.getInt("CNT2");
					
					break;
				} 
				
					HSSFCell dataCell = dataRow[i].createCell(1);
					HSSFCell dataCell2 = dataRow[i].createCell(2);
					dataCell.setCellValue(0.0);
					dataCell2.setCellValue(0.0);
					dataCell.setCellStyle(dataStyle);
					dataCell2.setCellStyle(dataStyle);

			}
		}

		HSSFCell sumCell = headrow2.createCell(0);
		HSSFCell sumCell2 = headrow2.createCell(1);
		HSSFCell sumCell3 = headrow2.createCell(2);

		sumCell.setCellValue("계");
		sumCell2.setCellValue((double) sum1);
		sumCell3.setCellValue((double) sum2);

		sumCell.setCellStyle(dataStyle);
		sumCell2.setCellStyle(dataStyle);
		sumCell3.setCellStyle(dataStyle);
		
    }

    protected void disastorStat(Map model, HSSFWorkbook wb) {
		int i = 0;
		int j = 0;
		int rowCnt = 0;
		
		// 시트 생성
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
		// 타이틀 셀에 삽입 => 긴급교통정보 처리건수 실적 (1행)
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		HSSFCell titleCell = titlerow.createCell(rowCnt);	
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // 행 병합
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		
		sheet1.setColumnWidth(0, 8000);
		sheet1.setColumnWidth(1, 8000);
		sheet1.setColumnWidth(2, 8000);
		rowCnt++;
		rowCnt++;
		rowCnt++;
		
		// 일자, 일별 총 제보건수, 재난제보 건수 (4행)
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
		headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font headStylefont = wb.createFont(); // 폰트 객체 생성
 	    headStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    headStyle.setFont(headStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    HSSFCell dayCell = headrow1.createCell(0);
	 	HSSFCell allImpCell = headrow1.createCell(1);
	 	HSSFCell brod5Cell = headrow1.createCell(2);
	 	
		// 일자, 총 긴급정보 건수, 5분내 방송처리 건수 (4행)
	 	dayCell.setCellValue("일자");
	 	allImpCell.setCellValue("일별 총 제보건수");
	 	brod5Cell.setCellValue("재난제보 건수");
	 	
	 	dayCell.setCellStyle(headStyle);
	 	allImpCell.setCellStyle(headStyle);
	 	brod5Cell.setCellStyle(headStyle);
	 	
		rowCnt++;
		HSSFRow headrow2 = sheet1.createRow(rowCnt);
		
		int sum1 = 0;
		int sum2 = 0;
		
		List data = (List) model.get("Data");
		rowCnt++;
		
		// 시작일, 종료일 가져오기
		String Sdate = (String) model.get("start_date");
		String Edate = (String) model.get("end_date");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		LocalDate startDate = LocalDate.parse(Sdate, formatter);
		LocalDate endDate = LocalDate.parse(Edate, formatter);
		
		int maxMon = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

	    List<String> dateList = new ArrayList<>(maxMon);

	    LocalDate currentDate = startDate;
	    while (!currentDate.isAfter(endDate)) {
	    	dateList.add(currentDate.format(formatter)); 
	        currentDate = currentDate.plusDays(1); 
	    }

	    DataFormat format = wb.createDataFormat();
	    
	    CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));
		
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
	    HSSFRow[] dataRow = new HSSFRow[maxMon];

		for (i = 0; i < maxMon; ++i) {
			dataRow[i] = sheet1.createRow(rowCnt + i);
			String inputDate = "";
			
			HSSFCell dateCell = dataRow[i].createCell(0);
			dateCell.setCellValue(dateList.get(i));
			dateCell.setCellStyle(dataStyle);
			
			inputDate = dateList.get(i);

			for (int k = 0; k < data.size(); ++k) {
				RecordDto record = (RecordDto) data.get(k);
				if (inputDate.equals(record.getString("KEY_DATE"))) {
					
					
					HSSFCell dataCell = dataRow[i].createCell(1);
					HSSFCell dataCell2 = dataRow[i].createCell(2);
					
					dataCell.setCellValue((double) record.getInt("CNT1"));
					dataCell2.setCellValue((double) record.getInt("CNT2"));
					
					dataCell.setCellStyle(dataStyle);
					dataCell2.setCellStyle(dataStyle);

					sum1 += record.getInt("CNT1");
					sum2 += record.getInt("CNT2");
					break;
				}

				HSSFCell dataCell = dataRow[i].createCell(1);
				HSSFCell dataCell2 = dataRow[i].createCell(2);
				dataCell.setCellValue(0.0);
				dataCell2.setCellValue(0.0);
				dataCell.setCellStyle(dataStyle);
				dataCell2.setCellStyle(dataStyle);
			}
		}

		HSSFCell sumCell = headrow2.createCell(0);
		HSSFCell sumCell2 = headrow2.createCell(1);
		HSSFCell sumCell3 = headrow2.createCell(2);

		sumCell.setCellValue("계");
		sumCell2.setCellValue((double) sum1);
		sumCell3.setCellValue((double) sum2);

		sumCell.setCellStyle(dataStyle);
		sumCell2.setCellStyle(dataStyle);
		sumCell3.setCellStyle(dataStyle);
    }

    
    
    //월별 제보자별 제보건수
	protected void muJebo(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		List headListMdata = (List) model.get("headList");
		List dataList = (List) model.get("Data");
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		
		HSSFCell titleCell = titlerow.createCell(rowCnt);
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));

		rowCnt++;
		sheet1.createRow(rowCnt);
		rowCnt++;
		
		CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용
		
		HSSFRow headrow2 = sheet1.createRow(rowCnt);
		
		HSSFCell headCell = headrow2.createCell(0);
		HSSFCell headCell2 = headrow2.createCell(1);
		HSSFCell headCell3 = headrow2.createCell(2);
		HSSFCell headCell4 = headrow2.createCell(3);
		HSSFCell headCell5 = headrow2.createCell(4);
		HSSFCell headCell6 = headrow2.createCell(5);
		
		headCell.setCellValue("ID");
		headCell2.setCellValue("이 름");
		headCell3.setCellValue("유형");
		headCell4.setCellValue("소속");
		headCell5.setCellValue("전화번호");
		headCell6.setCellValue("가입일자");
		
		headCell.setCellStyle(headStyle);
		headCell2.setCellStyle(headStyle);
		headCell3.setCellStyle(headStyle);
		headCell4.setCellStyle(headStyle);
		headCell5.setCellStyle(headStyle);
		headCell6.setCellStyle(headStyle);

		for (int k = 1; k <= 12; ++k) {
			HSSFCell headCell7 = headrow2.createCell(5 + k);
			headCell7.setCellValue(k + "월");
			headCell7.setCellStyle(headStyle);
		}

		rowCnt++;
		HSSFRow[] dataRow1 = new HSSFRow[headListMdata.size()];
		int[] monArr = new int[12];

		DataFormat format = wb.createDataFormat();

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    
		for (int i = 0; i < dataRow1.length; ++i) {
			RecordDto record = (RecordDto) headListMdata.get(i);
			dataRow1[i] = sheet1.createRow(rowCnt + i);
			
			HSSFCell dataCell = dataRow1[i].createCell(0);
			HSSFCell dataCell2 = dataRow1[i].createCell(1);
			HSSFCell dataCell3 = dataRow1[i].createCell(2);
			HSSFCell dataCell4 = dataRow1[i].createCell(3);
			HSSFCell dataCell5 = dataRow1[i].createCell(4);
			HSSFCell dataCell6 = dataRow1[i].createCell(5);
			
			dataCell.setCellValue(record.getString("ACT_ID"));
			dataCell2.setCellValue(record.getString("INFORMER_NAME"));
			dataCell3.setCellValue("통신원");
			dataCell4.setCellValue(record.getString("ORG_NAME"));
			dataCell5.setCellValue(record.getString("PHONE_CELL"));
			dataCell6.setCellValue(record.getString("REG_DATE"));
			
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
			dataCell3.setCellStyle(dataStyle);
			dataCell4.setCellStyle(dataStyle);
			dataCell5.setCellStyle(dataStyle);
			dataCell6.setCellStyle(dataStyle);

			for (int k = 0; k < dataList.size(); ++k) {
				RecordDto record2 = (RecordDto) dataList.get(k);

				for (int j = 1; j <= 12; ++j) {
					String j2 = "";
					if (j < 10) {
						j2 = "0" + Integer.toString(j);
					} else {
						j2 = Integer.toString(j);
					}

					if (record.getString("INFORMER_ID").equals(record2.getString("INFORMER_ID"))) {
						int inputNum = 0;
						if (record2.getString("KEY_DATE").equals(j2)) {
							inputNum = record2.getInt("CNT");
							if (dataRow1[i].getCell(5 + j) == null) {
								HSSFCell data2Cell = dataRow1[i].createCell(5 + j);
								data2Cell.setCellValue((double) inputNum);
								data2Cell.setCellStyle(dataStyle);
							}
						}

						monArr[j - 1] += inputNum;
					}
				}
			}
		}

		// 일부 셀 넓이 조절
		sheet1.setColumnWidth(0, 5000);
		sheet1.setColumnWidth(1, 6000);
		sheet1.setColumnWidth(3, 6000);
		sheet1.setColumnWidth(4, 6000);
		sheet1.setColumnWidth(5, 6000);
		
	}

	// 무제보자 현황
	protected void muJebo2(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		List headListMdata = (List) model.get("headList");
		List dataList = (List) model.get("Data");
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    HSSFCell titleCell = titlerow.createCell(rowCnt);
 	    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

 	    titleCell.setCellValue(model.get("titleName").toString());
 	    titleCell.setCellStyle(titleStyle);
 	   
 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	    
	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

		rowCnt++;
		
		HSSFRow headrow = sheet1.createRow(rowCnt);
		HSSFCell allCell = headrow.createCell(0);
		allCell.setCellValue("총 인원 : " + headListMdata.size() + "명");
		allCell.setCellStyle(mainStyle);
		
		rowCnt++;
		
		CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용
		
		HSSFRow headrow2 = sheet1.createRow(rowCnt);
		HSSFCell headCell = headrow2.createCell(0);
		HSSFCell headCell2 = headrow2.createCell(1);
		HSSFCell headCell3 = headrow2.createCell(2);
		HSSFCell headCell4 = headrow2.createCell(3);
		HSSFCell headCell5 = headrow2.createCell(4);
		HSSFCell headCell6 = headrow2.createCell(5);

		headCell.setCellValue("ID");
		headCell2.setCellValue("이 름");
		headCell3.setCellValue("유형");
		headCell4.setCellValue("소속");
		headCell5.setCellValue("전화번호");
		headCell6.setCellValue("가입일자");
		
		headCell.setCellStyle(headStyle);
		headCell2.setCellStyle(headStyle);
		headCell3.setCellStyle(headStyle);
		headCell4.setCellStyle(headStyle);
		headCell5.setCellStyle(headStyle);
		headCell6.setCellStyle(headStyle);
		
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    
		rowCnt++;
		HSSFRow[] dataRow1 = new HSSFRow[headListMdata.size()];
		int[] monArr = new int[12];

		for (int i = 0; i < dataRow1.length; ++i) {
			RecordDto record = (RecordDto) headListMdata.get(i);
			dataRow1[i] = sheet1.createRow(rowCnt + i);
			
			HSSFCell dataCell = dataRow1[i].createCell(0);
			HSSFCell dataCell2 = dataRow1[i].createCell(1);
			HSSFCell dataCell3 = dataRow1[i].createCell(2);
			HSSFCell dataCell4 = dataRow1[i].createCell(3);
			HSSFCell dataCell5 = dataRow1[i].createCell(4);
			HSSFCell dataCell6 = dataRow1[i].createCell(5);
			
			dataCell.setCellValue(record.getString("ACT_ID"));
			dataCell2.setCellValue(record.getString("INFORMER_NAME"));
			dataCell3.setCellValue("통신원");
			dataCell4.setCellValue(record.getString("ORG_NAME"));
			dataCell5.setCellValue(record.getString("PHONE_CELL"));
			dataCell6.setCellValue(record.getString("REG_DATE"));
			
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
			dataCell3.setCellStyle(dataStyle);
			dataCell4.setCellStyle(dataStyle);
			dataCell5.setCellStyle(dataStyle);
			dataCell6.setCellStyle(dataStyle);
		}

		sheet1.setColumnWidth(0, 5000);
		sheet1.setColumnWidth(1, 6000);
		sheet1.setColumnWidth(2, 5000);
		sheet1.setColumnWidth(3, 6000);
		sheet1.setColumnWidth(4, 6000);
		sheet1.setColumnWidth(5, 6000);

	}
	
	protected void standardInformUse(Map model, HSSFWorkbook wb) {
        List sheetNames = (List) model.get("sheetNames");
        List monthReceiptMain = (List) model.get("monthReceipt");
        List monthOurReceiptMain = (List) model.get("monthOurReceiptMain");
        List monthOtherReceiptMain = (List) model.get("monthOtherReceiptMain");
        List monthInformerMain = (List) model.get("monthInformerMain");
        List monthInformer1InformMain = (List) model.get("monthInformer1InformMain");
        String thDateTime = (String) model.get("start_date");
        for (int i = 0; i < sheetNames.size(); i++) {
            informUse(wb, (String) sheetNames.get(i), (List) monthReceiptMain.get(i), (List) monthOurReceiptMain.get(i), (List) monthOtherReceiptMain.get(i), (List) monthInformerMain.get(i), (List) monthInformer1InformMain.get(i), thDateTime);
        }
    }

	// 교통통신원 제보건수 및 접수직원 가공건수 실제 함수
		protected void informUse(HSSFWorkbook wb, String sheetNames, List monthReceiptMain, List monthOurReceiptMain,
				List monthOtherReceiptMain, List monthInformerMain, List monthInformer1InformMain, String thDateTime) {
			int rowCnt = 0;
			HSSFSheet sheet1 = wb.createSheet(sheetNames);
			HSSFRow titlerow = sheet1.createRow(rowCnt);
			
			CellStyle titleStyle = wb.createCellStyle();
	        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
	        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	 	    
	 	    Font font = wb.createFont(); // 폰트 객체 생성
	 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
	 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
			
	 	    HSSFCell titleCell = titlerow.createCell(rowCnt);
	 	    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
	 	    titleCell.setCellValue("통신원 제보건수 및 접수요원 가공건수");
	 	    titleCell.setCellStyle(titleStyle);	    
	 	    
			rowCnt++;
			HSSFRow headrow = sheet1.createRow(rowCnt);
			rowCnt++;
			HSSFRow rbRow = sheet1.createRow(rowCnt);
			rowCnt++;
			HSSFRow rbRow1 = sheet1.createRow(rowCnt);
			
			CellStyle headStyle = wb.createCellStyle();
	     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
	     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
	     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
	     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
	     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			
			Font headStyleF = wb.createFont(); // 폰트 객체 생성
			headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
			headStyleF.setFontName("굴림체");
			headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용
			
			HSSFCell headCell = headrow.createCell(1);
			HSSFCell headCell1 = headrow.createCell(2);
			headCell.setCellValue("총 제보건수");
			headCell.setCellStyle(headStyle);
			headCell1.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 2, rowCnt - 2, 1, 2));
			
			HSSFCell headCell2 = headrow.createCell(3);
			HSSFCell headCell22 = headrow.createCell(4);
			HSSFCell headCell23 = headrow.createCell(5);
			HSSFCell headCell24 = headrow.createCell(6);
			HSSFCell headCell25 = headrow.createCell(7);
			HSSFCell headCell26 = headrow.createCell(8);
			
			headCell2.setCellValue(sheetNames);
			headCell2.setCellStyle(headStyle);
			headCell22.setCellStyle(headStyle);
			headCell23.setCellStyle(headStyle);
			headCell24.setCellStyle(headStyle);
			headCell25.setCellStyle(headStyle);
			headCell26.setCellStyle(headStyle);
			
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 2, rowCnt - 2, 3, 8));
			
			HSSFCell headCell3 = rbRow.createCell(1);
			headCell3.setCellValue("월간");
			headCell3.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 1, 1));
			
			HSSFCell headCell4 = rbRow.createCell(2);
			headCell4.setCellValue("일간");
			headCell4.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 2, 2));
			
			HSSFCell headCell5 = rbRow.createCell(3);
			headCell5.setCellValue("제보건수(건)");
			headCell5.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, 3, 4));
			
			HSSFCell headCell6 = rbRow.createCell(5);
			headCell6.setCellValue("총인원(명)");
			headCell6.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 5, 5));
			
			HSSFCell headCell7 = rbRow.createCell(6);
			headCell7.setCellValue("1인당 제보건수(총인원 대비)");
			headCell7.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 6, 6));
			
			HSSFCell headCell8 = rbRow.createCell(7);
			headCell8.setCellValue("1건이상 제보자수");
			headCell8.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 7, 7));
			
			HSSFCell headCell9 = rbRow.createCell(8);
			headCell9.setCellValue("1인당 제보건수(1건이상 제보자대비)");
			headCell9.setCellStyle(headStyle);
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt, 8, 8));
			
			HSSFCell ourCell = rbRow1.createCell(3);
			HSSFCell otherCell = rbRow1.createCell(4);
			HSSFCell bottomCell = rbRow1.createCell(1);
			HSSFCell bottomCell2 = rbRow1.createCell(2);
			HSSFCell bottomCell3 = rbRow1.createCell(5);
			HSSFCell bottomCell4 = rbRow1.createCell(6);
			HSSFCell bottomCell5 = rbRow1.createCell(7);
			HSSFCell bottomCell6 = rbRow1.createCell(8);
			
			ourCell.setCellValue("자국");
			otherCell.setCellValue("타국");
			
			ourCell.setCellStyle(headStyle);
			otherCell.setCellStyle(headStyle);
			bottomCell.setCellStyle(headStyle);
			bottomCell2.setCellStyle(headStyle);
			bottomCell3.setCellStyle(headStyle);
			bottomCell4.setCellStyle(headStyle);
			bottomCell5.setCellStyle(headStyle);
			bottomCell6.setCellStyle(headStyle);
			
			
			int monthCnt = 12;
			rowCnt++;
			HSSFRow[] dataRow = new HSSFRow[monthCnt];
			int[] maxDay = new int[monthCnt];

			DataFormat format = wb.createDataFormat();
			 
			CellStyle dataStyle = wb.createCellStyle();
			dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
			dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
			dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
			dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			dataStyle.setDataFormat(format.getFormat("#,##0"));
			
	 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
	 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
	 	    
			int i;
			int statDate;
			for (i = 0; i < monthCnt; ++i) {
				dataRow[i] = sheet1.createRow(rowCnt);
				
				HSSFCell monCell = dataRow[i].createCell(0);
				monCell.setCellValue(i + 1 + "월");
				monCell.setCellStyle(dataStyle);
				
				Calendar ca = Calendar.getInstance();
				int thYear = Integer.parseInt(thDateTime.substring(0, 4));
				statDate = Integer.parseInt(thDateTime.substring(4, 6));
				ca.set(thYear, i, statDate);
				maxDay[i] = ca.getActualMaximum(5);
				rowCnt++;
			}

			DecimalFormat df = new DecimalFormat("##0.0");

			int j;
			RecordDto record;
			

			for (i = 0; i < monthReceiptMain.size(); ++i) {

				
				record = (RecordDto) monthReceiptMain.get(i);
				
				statDate = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));

				for (j = 0; j < monthCnt; ++j) {
					if (j == statDate - 1) {
						
						HSSFCell dataCell = dataRow[statDate - 1].createCell(1);
						HSSFCell dataCell2 = dataRow[statDate - 1].createCell(2);
						
						dataCell.setCellValue((double) record.getInt("CNT"));
						dataCell2.setCellValue(df.format(record.getDouble("CNT") / (double) maxDay[j]));
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
					} else {
						HSSFCell dataCell = dataRow[j].createCell(1);
						HSSFCell dataCell2 = dataRow[j].createCell(2);
						
						dataCell.setCellValue(0.0);
						dataCell2.setCellValue(0.0);
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
					}
				}
			}

			/*this.setRowData(monthOurReceiptMain, monthCnt, dataRow, 3);
			this.setRowData(monthOtherReceiptMain, monthCnt, dataRow, 4);*/

			for (int z = 0; z < monthOurReceiptMain.size(); z++) {
	            record = (RecordDto) monthOurReceiptMain.get(z);
	            int statDate2 = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));
	            for (int x = 0; x < monthCnt; x++) {
	                if (x == statDate2 - 1) {
	                	HSSFCell dateCell = dataRow[statDate2 - 1].createCell(3);
	                	dateCell.setCellValue(record.getInt("CNT"));
	                	dateCell.setCellStyle(dataStyle);
	                } else {
	                	HSSFCell dateCell = dataRow[x].createCell(3);
	                	 dateCell.setCellValue(0.0d);
	                	 dateCell.setCellStyle(dataStyle);
	                }
	            }
	        }
			
			for (int z = 0; z < monthOtherReceiptMain.size(); z++) {
	            record = (RecordDto) monthOtherReceiptMain.get(z);
	            int statDate2 = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));
	            for (int x = 0; x < monthCnt; x++) {
	                if (x == statDate2 - 1) {
	                	HSSFCell dateCell = dataRow[statDate2 - 1].createCell(4);
	                	dateCell.setCellValue(record.getInt("CNT"));
	                	dateCell.setCellStyle(dataStyle);
	                } else {
	                	HSSFCell dateCell = dataRow[x].createCell(4);
	                	 dateCell.setCellValue(0.0d);
	                	 dateCell.setCellStyle(dataStyle);
	                }
	            }
	        }
			
			
			
			for (i = 0; i < monthInformerMain.size(); ++i) {
				record = (RecordDto) monthInformerMain.get(i);
				statDate = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));

				for (j = 0; j < monthCnt; ++j) {
					if (j == statDate - 1) {
						HSSFCell dataCell = dataRow[statDate - 1].createCell(5);
						HSSFCell dataCell2 = dataRow[statDate - 1].createCell(6);		
						
						dataCell.setCellValue((double) record.getInt("CNT"));
						// 문제 발생 지점
						dataCell2.setCellValue(df.format(dataRow[statDate - 1].getCell(1).getNumericCellValue() / record.getDouble("CNT")));
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
						
					} else {
						HSSFCell dataCell = dataRow[j].createCell(5);
						HSSFCell dataCell2 = dataRow[j].createCell(6);		
						
						dataCell.setCellValue(0.0);
						dataCell2.setCellValue(0.0);
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
					}
				}
			}

			for (i = 0; i < monthInformer1InformMain.size(); ++i) {
				record = (RecordDto) monthInformer1InformMain.get(i);
				statDate = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));

				for (j = 0; j < monthCnt; ++j) {
					if (j == statDate - 1) {
						
						
						HSSFCell dataCell = dataRow[statDate - 1].createCell(7);
						HSSFCell dataCell2 = dataRow[statDate - 1].createCell(8);

						dataCell.setCellValue((double) record.getInt("CNT"));
						dataCell2.setCellValue(df
								.format(dataRow[statDate - 1].getCell(1).getNumericCellValue() / record.getDouble("CNT")));
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
						
					} else {
						HSSFCell dataCell = dataRow[j].createCell(7);
						HSSFCell dataCell2 = dataRow[j].createCell(8);
						
						dataCell.setCellValue(0.0);
						dataCell2.setCellValue(0.0);
						
						dataCell.setCellStyle(dataStyle);
						dataCell2.setCellStyle(dataStyle);
					}
					
					
					
					
				}
			}
			
			sheet1.setColumnWidth(1, 5500);
			sheet1.setColumnWidth(2, 5500);
			sheet1.setColumnWidth(3, 5500);
			sheet1.setColumnWidth(4, 5500);
			sheet1.setColumnWidth(5, 5500);
			sheet1.setColumnWidth(6, 5500);
			sheet1.setColumnWidth(7, 5500);
			sheet1.setColumnWidth(8, 5500);

		}
		
		protected void setRowData(List<?> data, int cnt, HSSFRow[] dataRow, int cellIndex) {
	 	    
	        for (int i = 0; i < data.size(); i++) {
	            RecordDto record = (RecordDto) data.get(i);
	            int statDate = Integer.parseInt(record.getString("STAT_MONTH").substring(4, 6));
	            for (int j = 0; j < cnt; j++) {
	                if (j == statDate - 1) {
	                    dataRow[statDate - 1].createCell(cellIndex).setCellValue(record.getInt("CNT"));
	                } else {
	                    dataRow[j].createCell(cellIndex).setCellValue(0.0d);
	                }
	            }
	        }
	    }

		
		
		
	// 교통정보 수집건수 및 활용실적 엑셀 다운로드
	protected void standartdReceiptUse(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		
		// 통신원 유형 갯수 데이터
		int eSize = (int)model.get("eSize");
		
		// 시작일 데이터
		String thDateTime = (String) model.get("start_date");
		
		// model에서 엑셀 시트명 가져오고, 시트 생성
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		// 대제목 넣을 행 생성
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		
		
		// 대제목 엑셀 스타일 설정
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
 	    // 대제목 설정 및 스타일 적용
 	    HSSFCell titleCell = titlerow.createCell(rowCnt);
 	    titleCell.setCellValue("교통정보 수집건수 및 활용실적");
 	    titleCell.setCellStyle(titleStyle);
 	    
 	    
 	    
 	    // 통신원 유형 데이터 가져오기
 	    List headData = (List) model.get("headList");
 	     
 	    int titleSize = (headData.size() * 2) + 2;
 	    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, titleSize));
 	   
		++rowCnt;
		
		CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용
		
		HSSFRow mainRow = sheet1.createRow(rowCnt);
		HSSFCell mainCell = mainRow.createCell(0);
		mainCell.setCellValue(model.get("sheetNames1").toString());
		mainCell.setCellStyle(mainStyle);
		
		++rowCnt;
		
		CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용 	
		
		HSSFRow headrow = sheet1.createRow(rowCnt);
		
		HSSFCell typeCell = headrow.createCell(0);
		HSSFCell sumCell =	headrow.createCell(1);	
		HSSFCell sumCell2 =	headrow.createCell(2);
		
		typeCell.setCellValue("구분");
		sumCell.setCellValue("계");
		
		typeCell.setCellStyle(headStyle);
		sumCell.setCellStyle(headStyle);
		sumCell2.setCellStyle(headStyle);

		int j;
		
		// 통신원 유형 갯수만큼 돌림
		for (j = 0; j < headData.size(); ++j) {
			RecordDto record = (RecordDto) headData.get(j);
			
			HSSFCell typeCell2 = headrow.createCell(j * 2 + 3);
			HSSFCell typeCell3 = headrow.createCell(j * 2 + 4);
			typeCell2.setCellValue(record.getString("CODE_NAME"));
			typeCell2.setCellStyle(headStyle);
			typeCell3.setCellStyle(headStyle);
		}

		++rowCnt;
		
		DataFormat format = wb.createDataFormat();
		
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));

 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		List headData1 = (List) model.get("useAllList");
		
		List<Integer> cntValues = new ArrayList<>();

		int sum = 0;
		//통신원 유형별 합계부분
		for (j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			
			HSSFCell dataCell = headrow1.createCell(j * 2 + 3);
			HSSFCell dataCell2 = headrow1.createCell(j * 2 + 4);
			
			dataCell.setCellValue((double) record.getInt("CNT"));
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
			
			cntValues.add(record.getInt("CNT"));
			sum += record.getInt("CNT");
		}

		HSSFCell dataSumCell = headrow1.createCell(1);
		dataSumCell.setCellValue((double) sum);
		dataSumCell.setCellStyle(dataStyle);

		int i;
		//통신원 유형별 합계 병합부분
		for (i = 0; i < headData.size() + 1; ++i) {
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, i * 2 + 1, i * 2 + 2));
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 2 + 1, i * 2 + 2));
		}

		++rowCnt;
		headrow1 = sheet1.createRow(rowCnt);

		for (j = 0; j < headData.size() + 1; ++j) {
			HSSFCell ourCell = headrow1.createCell(j * 2 + 1);
			HSSFCell otherCell = headrow1.createCell(j * 2 + 2);
			
			ourCell.setCellValue("자국");
			otherCell.setCellValue("타국");
			
			ourCell.setCellStyle(dataStyle);
			otherCell.setCellStyle(dataStyle);
			
			sheet1.setColumnWidth(j * 2 + 1, 4000);
			sheet1.setColumnWidth(j * 2 + 2, 4000);
		}

		++rowCnt;
		headrow1 = sheet1.createRow(rowCnt);
		headData1 = (List) model.get("useOurOther");
		int sumOur = 0;
		int sumOther = 0;

		for (j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			
			HSSFCell dataCell = headrow1.createCell(j + 3);
			dataCell.setCellValue((double) record.getInt("CNT"));
			dataCell.setCellStyle(dataStyle);
			
			if (record.getString("REGION_ID").equals("000")) {
				sumOther += record.getInt("CNT");
			} else {
				sumOur += record.getInt("CNT");
			}
		}

		HSSFCell oursumCell = headrow1.createCell(1);
		HSSFCell othersumCell = headrow1.createCell(2);
		
		oursumCell.setCellValue((double) sumOur);
		othersumCell.setCellValue((double) sumOther);
		
		oursumCell.setCellStyle(dataStyle);
		othersumCell.setCellStyle(dataStyle);
		
		++rowCnt;
		Calendar ca = Calendar.getInstance();
		int thYear = Integer.parseInt(thDateTime.substring(0, 4));
		int thMonth = Integer.parseInt(thDateTime.substring(4, 6));
		ca.set(thYear, i, thMonth);
		
		// 24-12-16 : 시작일 가져오기
		String Sdate = (String) model.get("start_date");
		String Edate = (String) model.get("end_date");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		LocalDate startDate = LocalDate.parse(Sdate, formatter);
		LocalDate endDate = LocalDate.parse(Edate, formatter);
		
		 int maxMon = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

	     List<String> dateList = new ArrayList<>(maxMon);

	     LocalDate currentDate = startDate;
	     while (!currentDate.isAfter(endDate)) {
	    	 dateList.add(currentDate.format(formatter)); 
	         currentDate = currentDate.plusDays(1); 
	     }
		
		HSSFRow[] dataRow = new HSSFRow[maxMon];
		int[] sumOurArr = new int[maxMon];
		int[] sumOtherArr = new int[maxMon];

		for(i = 0; i < maxMon; ++i) {
			dataRow[i] = sheet1.createRow(rowCnt + i);
			sumOurArr[i] = 0;
			sumOtherArr[i] = 0;
			
			HSSFCell dataCell = dataRow[i].createCell(0);
			dataCell.setCellValue(dateList.get(i));
			dataCell.setCellStyle(dataStyle);
		}
		
		
		
/*		for (i = 0; i < maxMon; ++i) 
			dataRow[i] = sheet1.createRow(rowCnt + i);
			sumOurArr[i] = 0;
			sumOtherArr[i] = 0;
			if (i < 9) {
				dataRow[i].createCell(0).setCellValue(date.concat("0").concat(Integer.toString(i + 1)));
			} else {
				dataRow[i].createCell(0).setCellValue(date.concat(Integer.toString(i + 1)));
			}
		}*/

		List data = (List) model.get("useDaily");

		String regionId;
		int statDate;
		int standardDate = Integer.parseInt(Sdate.substring(6, 8));
		int dataSize = data.size();
		
    	for (int k = 0; k < maxMon; k++) {

    		// 통신원 별로 없는 날짜를 위한 기본 데이터 및 스타일 셋팅 작업
    		for (j = 0; j < headData.size(); ++j) {
    	        HSSFCell cellOur = dataRow[k].createCell(j * 2 + 3);
    	        cellOur.setCellValue(0);
    	        cellOur.setCellStyle(dataStyle);

    	        HSSFCell cellOther = dataRow[k].createCell(j * 2 + 4);
    	        cellOther.setCellValue(0);
    	        cellOther.setCellStyle(dataStyle);
    	    }
    		
    		
    		for (i = 0; i < data.size(); ++i) {
				RecordDto record = (RecordDto) data.get(i);
				statDate = Integer.parseInt(record.getString("STAT_DATE").substring(6, 8));
				regionId = record.getString("REGION_ID");
				String informerType = record.getString("INFORMER_TYPE");
				
				String getStatDate = record.getString("STAT_DATE");
				
				int size = maxMon;
				int mSize = size;
				
				
				for (j = 0; j < headData.size(); ++j) {

					
					if(getStatDate.equals(dateList.get(k))){
						RecordDto record2 = (RecordDto) headData.get(j);
						if (informerType.equals(record2.getString("CODE"))) {
							if (regionId.equals("000")) {
								
								HSSFCell dataCell = dataRow[k].createCell(j * 2 + 4);
								dataCell.setCellValue((double) record.getInt("CNT"));
								dataCell.setCellStyle(dataStyle);
								
								sumOtherArr[k] += record.getInt("CNT");
							} else {
								
								HSSFCell dataCell = dataRow[k].createCell(j * 2 + 3);
								dataCell.setCellValue((double) record.getInt("CNT"));
								dataCell.setCellStyle(dataStyle);
								
								sumOurArr[k] += record.getInt("CNT");
							}
							
							break;
						} 
					} 
				}
			}
		}

		for (i = 0; i < maxMon; ++i) {
			HSSFCell dataCell = dataRow[i].createCell(1);
			HSSFCell dataCell2 = dataRow[i].createCell(2);
			
			dataCell.setCellValue((double) sumOurArr[i]);
			dataCell2.setCellValue((double) sumOtherArr[i]);
			
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
		}
		rowCnt = 0;
		
		sheet1.setColumnWidth(0, 5000);

		//활용실적 시작
		HSSFSheet sheet2 = wb.createSheet(model.get("sheetNames2").toString());
		titlerow = sheet2.createRow(rowCnt);
		
		
		HSSFCell titleCell2 = titlerow.createCell(rowCnt);
		titleCell2.setCellValue(model.get("sheetNames2").toString());
		titleCell2.setCellStyle(mainStyle);

		
		rowCnt = rowCnt + 1;
		headrow = sheet2.createRow(rowCnt);
		
		HSSFCell typeCell2 = headrow.createCell(0);
		HSSFCell sumCell3 = headrow.createCell(1);
		HSSFCell sumCell4 = headrow.createCell(2);
		HSSFCell sumCell5 = headrow.createCell(3);
		HSSFCell sumCell6 = headrow.createCell(4);
		
		typeCell2.setCellValue("구분");
		sumCell3.setCellValue("계");
		
		typeCell2.setCellStyle(headStyle);
		sumCell3.setCellStyle(headStyle);
		sumCell4.setCellStyle(headStyle);
		sumCell5.setCellStyle(headStyle);
		sumCell6.setCellStyle(headStyle);
		
		headData = (List) model.get("regionHead");

		for (j = 0; j < headData.size(); ++j) {
			RecordDto record = (RecordDto) headData.get(j);
			
			HSSFCell dataCell = headrow.createCell(j * 4 + 5);
			HSSFCell dataCell2 = headrow.createCell(j * 4 + 6);
			HSSFCell dataCell3 = headrow.createCell(j * 4 + 7);
			HSSFCell dataCell4 = headrow.createCell(j * 4 + 8);
			
			dataCell.setCellValue(record.getString("CODE_NAME"));
			dataCell.setCellStyle(headStyle);
			dataCell2.setCellStyle(headStyle);
			dataCell3.setCellStyle(headStyle);
			dataCell4.setCellStyle(headStyle);
		}

		for (i = 0; i < headData.size() + 1; ++i) {
			sheet2.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 4 + 1, i * 4 + 4));
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);
		headData1 = (List) model.get("sumMonth");
		data = (List) model.get("Data");
		
		sum = 0;
		int sumMid = 0;
		List<Integer> sumMidList = new ArrayList();

		
		/*for (j = 0; j < data.size(); ++j) {
			RecordDto record = (RecordDto) data.get(j);
			if (j % 4 == 0 && j > 0) {
				sumMidList.add(sumMid);
				sumMid = 0;
				sumMid = sumMid + record.getInt("CNT");
			} else if (j + 1 == headData1.size()) {
				sumMid += record.getInt("CNT");
				sumMidList.add(sumMid);
			} else {
				sumMid += record.getInt("CNT");
			}

			sum += record.getInt("CNT");
		}*/
		
		int[] sumList = new int[eSize];
		
		// 구분 > 합계들 나누기 및 구하기
		for(j = 0; j < data.size(); j++) {
			RecordDto record = (RecordDto) data.get(j);		
			int informerType = record.getInt("INFORMER_TYPE"); // 해당 데이터의 통신원 유형 가져오기
			if(informerType<sumList.length) {
				sumList[informerType] += record.getInt("CNT");
			}
			sum += record.getInt("CNT");
		}
		
		// 구분 > 계 (3행)
		HSSFCell sumCells2 = headrow1.createCell(1);
		sumCells2.setCellValue((double) sum);
		sumCells2.setCellStyle(dataStyle);
		
		// 구분 > 통신원, 경찰제보처, 시민, 직원 등 합계 (3행)
		int sumSendY;
		
		// 수정 전 ( ArrayIndexOutOfBoundsException: 8 오류 발생하여 for 값을 8로 고정 )
		for (sumSendY = 0; sumSendY < cntValues.size(); ++sumSendY) {
			
			HSSFCell dataCell = headrow1.createCell(sumSendY * 4 + 5);
			HSSFCell dataCell2 = headrow1.createCell(sumSendY * 4 + 6);
			HSSFCell dataCell3 = headrow1.createCell(sumSendY * 4 + 7);
			HSSFCell dataCell4 = headrow1.createCell(sumSendY * 4 + 8);
			
			dataCell.setCellValue(sumList[sumSendY]);
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
			dataCell3.setCellStyle(dataStyle);
			dataCell4.setCellStyle(dataStyle);
		}
		
		// 수정 후 값을 8로 고정시켜 sumSendY가 7을 넘지 않도록 설정
		/*for (sumSendY = 0; sumSendY < 8; ++sumSendY) {
			headrow1.createCell(sumSendY * 4 + 5).setCellValue(sumList[sumSendY]);
		}*/

		for (i = 0; i < headData.size() + 1; ++i) {
			sheet2.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 4 + 1, i * 4 + 4));
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);

		for (j = 0; j < headData.size() + 1; ++j) {
			
			HSSFCell dataCell = headrow1.createCell(j * 4 + 1);
			HSSFCell dataCell2 = headrow1.createCell(j * 4 + 2);
			HSSFCell dataCell3 = headrow1.createCell(j * 4 + 3);
			HSSFCell dataCell4 = headrow1.createCell(j * 4 + 4);
			
			dataCell.setCellValue("방송자료");
			dataCell2.setCellValue("자체처리");
			dataCell3.setCellValue("안내");
			dataCell4.setCellValue("기관통보");
			
			dataCell.setCellStyle(headStyle);
			dataCell2.setCellStyle(headStyle);
			dataCell3.setCellStyle(headStyle);
			dataCell4.setCellStyle(headStyle);
			
			sheet2.setColumnWidth(j * 4 + 1, 4000);
			sheet2.setColumnWidth(j * 4 + 2, 4000);
			sheet2.setColumnWidth(j * 4 + 3, 4000);
			sheet2.setColumnWidth(j * 4 + 4, 4000);
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);
		headData1 = (List) model.get("sumMonth");
		
		sumSendY = 0;
		int sumSendN = 0;
		int sumA07 = 0;
		int sumA08 = 0;

		String titleStr;
/*		for (i = 0; i < headData1.size(); ++i) {
			RecordDto record = (RecordDto) headData1.get(i);
			RecordDto record = (RecordDto) data.get(i);
			regionId = record.getString("CODE");
			titleStr = record.getString("SEQ");

			for (j = 0; j < headData.size(); ++j) {
				RecordDto recordType = (RecordDto) headData.get(j);
				RecordDto recordType = (RecordDto) data.get(j);
				if (recordType.getString("CODE").equals(regionId)) {
					if (titleStr.equals("B")) {
						headrow1.createCell(j * 4 + 5).setCellValue("허허허허허허");
						sumSendY += record.getInt("CNT");
					} else if (titleStr.equals("NB")) {
						headrow1.createCell(j * 4 + 6).setCellValue("허허허허허허");
						sumSendN += record.getInt("CNT");
					} else if (titleStr.equals("A07")) {
						headrow1.createCell(j * 4 + 7).setCellValue("허허허허허허");
						sumA07 += record.getInt("CNT");
					} else if (titleStr.equals("A08")) {
						headrow1.createCell(j * 4 + 8).setCellValue("허허허허허허");
						sumA08 += record.getInt("CNT");
					}
				}
			}
		}*/
		
		int sSize = 4 * (eSize+1);
		int[] sumList1 = new int[sSize];
		
		for(j = 0; j < data.size(); j++) {
			RecordDto record = (RecordDto) data.get(j);		
			int informerType = record.getInt("INFORMER_TYPE"); // 해당 데이터의 통신원 유형 가져오기
			String type = record.getString("SEQ");

			if(type.equals("B")) {
				sumList1[informerType * 4 + 0] += record.getInt("CNT");
				sumSendY += record.getInt("CNT");
 			} else if(type.equals("NB")) {
 				sumList1[informerType * 4 + 1] += record.getInt("CNT");
 				sumSendN += record.getInt("CNT");
 			} else if(type.equals("A07")) {
 				sumList1[informerType * 4 + 2] += record.getInt("CNT");
 				sumA07 += record.getInt("CNT");
 			} else {
 				sumList1[informerType * 4 + 3] += record.getInt("CNT");
 				sumA08 += record.getInt("CNT");
 			}
		}
		
		int sumCnt = (headData.size()) * 4;
		
		for(int z = 0; z < sumCnt ; z++) {
			HSSFCell dataCell = headrow1.createCell(z + 5);
			/*dataCell.setCellValue(sumList1[z]);*/
			dataCell.setCellValue(sumList1[z]);
			dataCell.setCellStyle(dataStyle);
		}
		
		// 전체 분류별 합계
		HSSFCell sumSendYCell = headrow1.createCell(1);
		HSSFCell sumSendNCell = headrow1.createCell(2);
		HSSFCell sumA07Cell = headrow1.createCell(3);
		HSSFCell sumA08Cell = headrow1.createCell(4);
		
		
		sumSendYCell.setCellValue((double) sumSendY);
		sumSendNCell.setCellValue((double) sumSendN);
		sumA07Cell.setCellValue((double) sumA07);
		sumA08Cell.setCellValue((double) sumA08);
		
		sumSendYCell.setCellStyle(dataStyle);
		sumSendNCell.setCellStyle(dataStyle);
		sumA07Cell.setCellStyle(dataStyle);
		sumA08Cell.setCellStyle(dataStyle);
		
		
		++rowCnt;
		HSSFRow[] dataRow1 = new HSSFRow[maxMon];
		int[] sumSendYArr = new int[maxMon];
		int[] sumSendNArr = new int[maxMon];
		int[] sumA07Arr = new int[maxMon];
		int[] sumA08Arr = new int[maxMon];

		for (i = 0; i < maxMon; ++i) {
			dataRow1[i] = sheet2.createRow(rowCnt + i);
			sumSendYArr[i] = 0;
			sumSendNArr[i] = 0;
			sumA07Arr[i] = 0;
			sumA08Arr[i] = 0;
			
			// 날짜 넣기
			HSSFCell dataCell = dataRow1[i].createCell(0);
			dataCell.setCellValue(dateList.get(i));
			dataCell.setCellStyle(dataStyle);
		}


		for (int k = 0; k < dataRow1.length; ++k) {
			for (j = 0; j < headData.size(); ++j) {
				RecordDto recordType = (RecordDto) headData.get(j);

				for (i = 0; i < data.size(); ++i) {
					RecordDto record = (RecordDto) data.get(i);
					statDate = Integer.parseInt(record.getString("STAT_DATE").substring(6, 8));
					
					
					regionId = record.getString("INFORMER_TYPE");
					titleStr = record.getString("SEQ");
					String getStatDate = record.getString("STAT_DATE");
					
					
					// 아래 부터는 일자, 통신원 종류별 값임(합계 X)
					if(getStatDate.equals(dateList.get(k))) {
							if (regionId.equals(recordType.getString("CODE"))) {
								
								// 방송자료일 때
								if (titleStr.equals("B")) {
									HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 5);				
									dataCell.setCellValue((double) record.getInt("CNT"));
									dataCell.setCellStyle(dataStyle);
									
									sumSendYArr[k] += record.getInt("CNT");
								} else if (titleStr.equals("NB")) { // 자체처리일 때
									HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 6);				
									dataCell.setCellValue((double) record.getInt("CNT"));
									dataCell.setCellStyle(dataStyle);

									sumSendNArr[k] += record.getInt("CNT");
								} else if (titleStr.equals("A07")) { // 기관통보일 때
									HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 7);				
									dataCell.setCellValue((double) record.getInt("CNT"));
									dataCell.setCellStyle(dataStyle);
									
									sumA07Arr[k] += record.getInt("CNT");
								} else if (titleStr.equals("A08")) { // 안내일 때
									HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 8);				
									dataCell.setCellValue((double) record.getInt("CNT"));
									dataCell.setCellStyle(dataStyle);
									
									sumA08Arr[k] += record.getInt("CNT");
								}
							}
						} else {
							if (dataRow1[k].getCell(j * 4 + 5) == null) {
								HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 5);
								dataCell.setCellValue(0);
								dataCell.setCellStyle(dataStyle);
								
								sumSendYArr[k] += 0;
							}

							if (dataRow1[k].getCell(j * 4 + 6) == null) {
								
								HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 6);
								dataCell.setCellValue(0);
								dataCell.setCellStyle(dataStyle);

								sumSendYArr[k] += 0;
							}

							if (dataRow1[k].getCell(j * 4 + 7) == null) {
								HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 7);
								dataCell.setCellValue(0);
								dataCell.setCellStyle(dataStyle);

								sumSendYArr[k] += 0;
							}

							if (dataRow1[k].getCell(j * 4 + 8) == null) {
								HSSFCell dataCell = dataRow1[k].createCell(j * 4 + 8);
								dataCell.setCellValue(0);
								dataCell.setCellStyle(dataStyle);

								sumSendYArr[k] += 0;
							}
						}
					}
				}
			}

		// 구분 > 계 > 합계 넣기
		for (i = 0; i < maxMon; ++i) {
			HSSFCell dataCell = dataRow1[i].createCell(1);
			HSSFCell dataCell2 = dataRow1[i].createCell(2);
			HSSFCell dataCell3 = dataRow1[i].createCell(3);
			HSSFCell dataCell4 = dataRow1[i].createCell(4);
			
			dataCell.setCellValue((double) sumSendYArr[i]);
			dataCell2.setCellValue((double) sumSendNArr[i]);
			dataCell3.setCellValue((double) sumA07Arr[i]);
			dataCell4.setCellValue((double) sumA08Arr[i]);
			
			dataCell.setCellStyle(dataStyle);
			dataCell2.setCellStyle(dataStyle);
			dataCell3.setCellStyle(dataStyle);
			dataCell4.setCellStyle(dataStyle);
		}
		
		
		// 셀 넓이 조절하기
		sheet2.setColumnWidth(0, 5000);
		

	}
	
	/*protected void standartdReceiptUse(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		String thDateTime = (String) model.get("start_date");
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		titlerow.createCell(rowCnt).setCellValue("교통정보 수집건수 및 활용실적");
		++rowCnt;
		HSSFRow mainRow = sheet1.createRow(rowCnt);
		mainRow.createCell(rowCnt).setCellValue(model.get("sheetNames1").toString());
		++rowCnt;
		HSSFRow headrow = sheet1.createRow(rowCnt);
		headrow.createCell(0).setCellValue("구분");
		headrow.createCell(1).setCellValue("계");
		List headData = (List) model.get("headList");

		int j;
		for (j = 0; j < headData.size(); ++j) {
			RecordDto record = (RecordDto) headData.get(j);
			headrow.createCell(j * 2 + 3).setCellValue(record.getString("CODE_NAME"));
		}

		++rowCnt;
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		List headData1 = (List) model.get("useAllList");
		int sum = 0;

		for (j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			headrow1.createCell(j * 2 + 3).setCellValue((double) record.getInt("CNT"));
			sum += record.getInt("CNT");
		}

		headrow1.createCell(1).setCellValue((double) sum);

		int i;
		for (i = 0; i < headData.size() + 1; ++i) {
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt - 1, rowCnt - 1, i * 2 + 1, i * 2 + 2));
			sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 2 + 1, i * 2 + 2));
		}

		++rowCnt;
		headrow1 = sheet1.createRow(rowCnt);

		for (j = 0; j < headData.size() + 1; ++j) {
			headrow1.createCell(j * 2 + 1).setCellValue("자국");
			headrow1.createCell(j * 2 + 2).setCellValue("타국");
		}

		++rowCnt;
		headrow1 = sheet1.createRow(rowCnt);
		headData1 = (List) model.get("useOurOther");
		int sumOur = 0;
		int sumOther = 0;

		for (j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			headrow1.createCell(j + 3).setCellValue((double) record.getInt("CNT"));
			if (record.getString("REGION_ID").equals("000")) {
				sumOther += record.getInt("CNT");
			} else {
				sumOur += record.getInt("CNT");
			}
		}

		headrow1.createCell(1).setCellValue((double) sumOur);
		headrow1.createCell(2).setCellValue((double) sumOther);
		++rowCnt;
		Calendar ca = Calendar.getInstance();
		int thYear = Integer.parseInt(thDateTime.substring(0, 4));
		int thMonth = Integer.parseInt(thDateTime.substring(4, 6));
		ca.set(thYear, i, thMonth);
		int maxMon = ca.getActualMaximum(5);
		
		// 24-12-16 : 시작일 가져오기
		String Sdate = (String) model.get("start_date");
		
		// 24-12-16 : 시작일 슬라이스
		String date = Sdate.substring(0, 6);
		
		HSSFRow[] dataRow = new HSSFRow[maxMon];
		int[] sumOurArr = new int[maxMon];
		int[] sumOtherArr = new int[maxMon];

		for (i = 0; i < maxMon; ++i) {
			dataRow[i] = sheet1.createRow(rowCnt + i);
			sumOurArr[i] = 0;
			sumOtherArr[i] = 0;
			if (i < 9) {
				dataRow[i].createCell(0).setCellValue(date.concat("0").concat(Integer.toString(i + 1)));
			} else {
				dataRow[i].createCell(0).setCellValue(date.concat(Integer.toString(i + 1)));
			}
		}

		List data = (List) model.get("useDaily");

		String regionId;
		int statDate;
		for (i = 0; i < data.size(); ++i) {
			RecordDto record = (RecordDto) data.get(i);
			statDate = Integer.parseInt(record.getString("STAT_DATE").substring(6, 8));
			regionId = record.getString("REGION_ID");
			String informerType = record.getString("INFORMER_TYPE");

			for (j = 0; j < headData.size(); ++j) {
				RecordDto record2 = (RecordDto) headData.get(j);
				if (informerType.equals(record2.getString("CODE"))) {
					if (regionId.equals("000")) {
						dataRow[statDate - 1].createCell(j * 2 + 4).setCellValue((double) record.getInt("CNT"));
						sumOtherArr[statDate - 1] += record.getInt("CNT");
					} else {
						dataRow[statDate - 1].createCell(j * 2 + 3).setCellValue((double) record.getInt("CNT"));
						sumOurArr[statDate - 1] += record.getInt("CNT");
					}
					//break;
				}
			}
		}

		for (i = 0; i < maxMon; ++i) {
			dataRow[i].createCell(1).setCellValue((double) sumOurArr[i]);
			dataRow[i].createCell(2).setCellValue((double) sumOtherArr[i]);
		}

		rowCnt = 0;
		HSSFSheet sheet2 = wb.createSheet(model.get("sheetNames2").toString());
		titlerow = sheet2.createRow(rowCnt);
		titlerow.createCell(rowCnt).setCellValue(model.get("sheetNames2").toString());
		rowCnt = rowCnt + 1;
		headrow = sheet2.createRow(rowCnt);
		headrow.createCell(0).setCellValue("구분");
		headrow.createCell(1).setCellValue("계");
		headData = (List) model.get("regionHead");

		for (j = 0; j < headData.size(); ++j) {
			RecordDto record = (RecordDto) headData.get(j);
			headrow.createCell(j * 4 + 5).setCellValue(record.getString("CODE_NAME"));
		}

		for (i = 0; i < headData.size() + 1; ++i) {
			sheet2.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 4 + 1, i * 4 + 4));
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);
		headData1 = (List) model.get("sumMonth");
		sum = 0;
		int sumMid = 0;
		List<Integer> sumMidList = new ArrayList();

		for (j = 0; j < headData1.size(); ++j) {
			RecordDto record = (RecordDto) headData1.get(j);
			if (j % 4 == 0 && j > 0) {
				sumMidList.add(sumMid);
				sumMid = 0;
				sumMid = sumMid + record.getInt("CNT");
			} else if (j + 1 == headData1.size()) {
				sumMid += record.getInt("CNT");
				sumMidList.add(sumMid);
			} else {
				sumMid += record.getInt("CNT");
			}

			sum += record.getInt("CNT");
		}

		headrow1.createCell(1).setCellValue((double) sum);

		int sumSendY;
		for (sumSendY = 0; sumSendY < sumMidList.size(); ++sumSendY) {
			headrow1.createCell(sumSendY * 4 + 5).setCellValue((double) (Integer) sumMidList.get(sumSendY));
		}

		for (i = 0; i < headData.size() + 1; ++i) {
			sheet2.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, i * 4 + 1, i * 4 + 4));
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);

		for (j = 0; j < headData.size() + 1; ++j) {
			headrow1.createCell(j * 4 + 1).setCellValue("방송자료");
			headrow1.createCell(j * 4 + 2).setCellValue("자체처리");
			headrow1.createCell(j * 4 + 3).setCellValue("안내");
			headrow1.createCell(j * 4 + 4).setCellValue("기관통보");
		}

		++rowCnt;
		headrow1 = sheet2.createRow(rowCnt);
		headData1 = (List) model.get("sumMonth");
		sumSendY = 0;
		int sumSendN = 0;
		int sumA07 = 0;
		int sumA08 = 0;

		String titleStr;
		for (i = 0; i < headData1.size(); ++i) {
			RecordDto record = (RecordDto) headData1.get(i);
			regionId = record.getString("CODE");
			titleStr = record.getString("SEQ");

			for (j = 0; j < headData.size(); ++j) {
				RecordDto recordType = (RecordDto) headData.get(j);
				if (recordType.getString("CODE").equals(regionId)) {
					if (titleStr.equals("B")) {
						headrow1.createCell(j * 4 + 5).setCellValue((double) record.getInt("CNT"));
						sumSendY += record.getInt("CNT");
					} else if (titleStr.equals("NB")) {
						headrow1.createCell(j * 4 + 6).setCellValue((double) record.getInt("CNT"));
						sumSendN += record.getInt("CNT");
					} else if (titleStr.equals("A07")) {
						headrow1.createCell(j * 4 + 7).setCellValue((double) record.getInt("CNT"));
						sumA07 += record.getInt("CNT");
					} else if (titleStr.equals("A08")) {
						headrow1.createCell(j * 4 + 8).setCellValue((double) record.getInt("CNT"));
						sumA08 += record.getInt("CNT");
					}
				}
			}
		}

		headrow1.createCell(1).setCellValue((double) sumSendY);
		headrow1.createCell(2).setCellValue((double) sumSendN);
		headrow1.createCell(3).setCellValue((double) sumA07);
		headrow1.createCell(4).setCellValue((double) sumA08);
		++rowCnt;
		HSSFRow[] dataRow1 = new HSSFRow[maxMon];
		int[] sumSendYArr = new int[maxMon];
		int[] sumSendNArr = new int[maxMon];
		int[] sumA07Arr = new int[maxMon];
		int[] sumA08Arr = new int[maxMon];

		for (i = 0; i < maxMon; ++i) {
			dataRow1[i] = sheet2.createRow(rowCnt + i);
			sumSendYArr[i] = 0;
			sumSendNArr[i] = 0;
			sumA07Arr[i] = 0;
			sumA08Arr[i] = 0;
			if (i < 9) {
				dataRow1[i].createCell(0).setCellValue(date.concat("0").concat(Integer.toString(i + 1)));
			} else {
				dataRow1[i].createCell(0).setCellValue(date.concat(Integer.toString(i + 1)));
			}
		}

		data = (List) model.get("Data");

		for (int k = 0; k < dataRow1.length; ++k) {
			for (j = 0; j < headData.size(); ++j) {
				RecordDto recordType = (RecordDto) headData.get(j);

				for (i = 0; i < data.size(); ++i) {
					RecordDto record = (RecordDto) data.get(i);
					statDate = Integer.parseInt(record.getString("STAT_DATE").substring(6, 8));
					regionId = record.getString("INFORMER_TYPE");
					titleStr = record.getString("SEQ");
					if (statDate - 1 == k) {
						if (regionId.equals(recordType.getString("CODE"))) {
							if (titleStr.equals("B")) {
								dataRow1[statDate - 1].createCell(j * 4 + 5)
										.setCellValue((double) record.getInt("CNT"));
								sumSendYArr[statDate - 1] += record.getInt("CNT");
							} else if (titleStr.equals("NB")) {
								dataRow1[statDate - 1].createCell(j * 4 + 6)
										.setCellValue((double) record.getInt("CNT"));
								sumSendNArr[statDate - 1] += record.getInt("CNT");
							} else if (titleStr.equals("A07")) {
								dataRow1[statDate - 1].createCell(j * 4 + 7)
										.setCellValue((double) record.getInt("CNT"));
								sumA07Arr[statDate - 1] += record.getInt("CNT");
							} else if (titleStr.equals("A08")) {
								dataRow1[statDate - 1].createCell(j * 4 + 8)
										.setCellValue((double) record.getInt("CNT"));
								sumA08Arr[statDate - 1] += record.getInt("CNT");
							}
						}
					} else {
						if (dataRow1[k].getCell(j * 4 + 5) == null) {
							dataRow1[k].createCell(j * 4 + 5).setCellValue(0.0);
							sumSendYArr[k] += 0;
						}

						if (dataRow1[k].getCell(j * 4 + 6) == null) {
							dataRow1[k].createCell(j * 4 + 6).setCellValue(0.0);
							sumSendYArr[k] += 0;
						}

						if (dataRow1[k].getCell(j * 4 + 7) == null) {
							dataRow1[k].createCell(j * 4 + 7).setCellValue(0.0);
							sumSendYArr[k] += 0;
						}

						if (dataRow1[k].getCell(j * 4 + 8) == null) {
							dataRow1[k].createCell(j * 4 + 8).setCellValue(0.0);
							sumSendYArr[k] += 0;
						}
					}
				}
			}
		}

		for (i = 0; i < maxMon; ++i) {
			dataRow1[i].createCell(1).setCellValue((double) sumSendYArr[i]);
			dataRow1[i].createCell(2).setCellValue((double) sumSendNArr[i]);
			dataRow1[i].createCell(3).setCellValue((double) sumA07Arr[i]);
			dataRow1[i].createCell(4).setCellValue((double) sumA08Arr[i]);
		}

	}*/

	
	
	
	// 돌발 교통정보 제공실적
    protected void nationalIncident(Map model, HSSFWorkbook wb) {
        List dataList = (List) model.get("Data");
        HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
        
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    
        HSSFRow titlerow = sheet1.createRow(0);
        
        HSSFCell titleCell = titlerow.createCell(0);
        titleCell.setCellValue(model.get("titleName").toString());
        titleCell.setCellStyle(titleStyle);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        
        int rowCnt = 0 + 1;
        
        CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

        HSSFRow headrow = sheet1.createRow(rowCnt);
        
        HSSFCell headCell = headrow.createCell(1);
        HSSFCell headCell2 = headrow.createCell(2);
        HSSFCell headCell3 = headrow.createCell(3);
        HSSFCell headCell4 = headrow.createCell(4);
        HSSFCell headCell5 = headrow.createCell(5);
        HSSFCell headCell6 = headrow.createCell(6);
        
        headCell.setCellValue("합계");
        headCell2.setCellValue("공사");
        headCell3.setCellValue("행사");
        headCell4.setCellValue("사고");
        headCell5.setCellValue("고장");
        headCell6.setCellValue("기타");
        
        headCell.setCellStyle(headStyle);
        headCell2.setCellStyle(headStyle);
        headCell3.setCellStyle(headStyle);
        headCell4.setCellStyle(headStyle);
        headCell5.setCellStyle(headStyle);
        headCell6.setCellStyle(headStyle);
        
        DataFormat format = wb.createDataFormat();
        
        CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setDataFormat(format.getFormat("#,##0"));

 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

        
        int rowCnt2 = rowCnt + 1;
        HSSFRow headrow2 = sheet1.createRow(rowCnt2);
        
        HSSFCell sumCell = headrow2.createCell(0);
        sumCell.setCellValue("계");
        sumCell.setCellStyle(headStyle);
        
        int sumsc = 0;
        int sum1 = 0;
        int sum2 = 0;
        int sum3 = 0;
        int sum4 = 0;
        int sum5 = 0;
        int rowCnt3 = rowCnt2 + 1;
        HSSFRow[] dataRow = new HSSFRow[12];
        String thDateTime = model.get("start_date").toString();
        int[] maxDay = new int[12];
        for (int i = 0; i < 12; i++) {
            dataRow[i] = sheet1.createRow(rowCnt3);
            
            HSSFCell dateCell = dataRow[i].createCell(0);
            dateCell.setCellValue(String.valueOf(i + 1) + "월");
            dateCell.setCellStyle(dataStyle);
            
            Calendar ca = Calendar.getInstance();
            int thYear = Integer.parseInt(thDateTime.substring(0, 4));
            int thMonth = Integer.parseInt(thDateTime.substring(4, 6));
            ca.set(thYear, i, thMonth);
            maxDay[i] = ca.getActualMaximum(5);
            rowCnt3++;
        }
        new DecimalFormat("##0.0");
        for (int i2 = 0; i2 < dataList.size(); i2++) {
            RecordDto record = (RecordDto) dataList.get(i2);
            int statDate = Integer.parseInt(record.getString("STAT_MONTH"));
            for (int j = 0; j < 12; j++) {
                if (j == statDate - 1) {
                	HSSFCell dataCell = dataRow[statDate - 1].createCell(1);
                	HSSFCell dataCell2 = dataRow[statDate - 1].createCell(2);
                	HSSFCell dataCell3 = dataRow[statDate - 1].createCell(3);
                	HSSFCell dataCell4 = dataRow[statDate - 1].createCell(4);
                	HSSFCell dataCell5 = dataRow[statDate - 1].createCell(5);
                	HSSFCell dataCell6 = dataRow[statDate - 1].createCell(6);
                	
                	dataCell.setCellValue(record.getInt("SUMC"));
                	dataCell2.setCellValue(record.getInt("CNT1"));
                	dataCell3.setCellValue(record.getInt("CNT2"));
                	dataCell4.setCellValue(record.getInt("CNT3"));
                	dataCell5.setCellValue(record.getInt("CNT4"));
                	dataCell6.setCellValue(record.getInt("CNT5"));
                    
                	dataCell.setCellStyle(dataStyle);
                	dataCell2.setCellStyle(dataStyle);
                	dataCell3.setCellStyle(dataStyle);
                	dataCell4.setCellStyle(dataStyle);
                	dataCell5.setCellStyle(dataStyle);
                	dataCell6.setCellStyle(dataStyle);
                	
                    sumsc += record.getInt("SUMC");
                    sum1 += record.getInt("CNT1");
                    sum2 += record.getInt("CNT2");
                    sum3 += record.getInt("CNT3");
                    sum4 += record.getInt("CNT4");
                    sum5 += record.getInt("CNT5");
                }
            }
        }
        
        HSSFCell sumCell1 = headrow2.createCell(1);
        HSSFCell sumCell2 = headrow2.createCell(2);
        HSSFCell sumCell3 = headrow2.createCell(3);
        HSSFCell sumCell4 = headrow2.createCell(4);
        HSSFCell sumCell5 = headrow2.createCell(5);
        HSSFCell sumCell6 = headrow2.createCell(6);
        
        sumCell1.setCellValue(sumsc);
        sumCell2.setCellValue(sum1);
        sumCell3.setCellValue(sum2);
        sumCell4.setCellValue(sum3);
        sumCell5.setCellValue(sum4);
        sumCell6.setCellValue(sum5);
        
        sumCell1.setCellStyle(dataStyle);
        sumCell2.setCellStyle(dataStyle);
        sumCell3.setCellStyle(dataStyle);
        sumCell4.setCellStyle(dataStyle);
        sumCell5.setCellStyle(dataStyle);
        sumCell6.setCellStyle(dataStyle);
    }

    
    // 교통정보 수집원 현황
	protected void standardInformer(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		List dataList = (List) model.get("Data");
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

		sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 12));
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		HSSFCell titleCell = titlerow.createCell(rowCnt);
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		
		CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용
		
		rowCnt++;
		HSSFRow headrow = sheet1.createRow(rowCnt);
		HSSFCell dateCell = headrow.createCell(0);
		dateCell.setCellValue(model.get("start_date").toString());
		sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 12));
		dateCell.setCellStyle(mainStyle);
		
		rowCnt++;
		int cellCnt = 4;
		int nwRowCnt = rowCnt;
		int addCellCnt = 0;
		int bfMonSum = 0;
		int thMonSum = 0;
		int addSubSum = 0;
		int wiSum = 0;
		int haeSum = 0;
		
		CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용
		
		DataFormat format = wb.createDataFormat();
		
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));

 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    
		for (int k = 0; k < dataList.size(); ++k) {
			RecordDto record = (RecordDto) dataList.get(k);
			String bfType = "";
			String nwType = record.getString("IND_NAME");
			if (k > 0) {
				RecordDto rcd = (RecordDto) dataList.get(k - 1);
				bfType = rcd.getString("IND_NAME");
				if (!nwType.equals(bfType)) {
					rowCnt = nwRowCnt + 1;
					
					HSSFRow sumRow = sheet1.getRow(rowCnt);
					HSSFCell sumCell = sumRow.createCell(2);
					sumCell.setCellValue((double) bfMonSum);
					sumCell.setCellStyle(dataStyle);
					sumRow.createCell(3).setCellStyle(dataStyle);
					rowCnt++;
					
					HSSFRow sumRow2 = sheet1.getRow(rowCnt);
					HSSFCell sumCell2 = sumRow2.createCell(2);
					sumCell2.setCellValue((double) thMonSum);
					sumCell2.setCellStyle(dataStyle);
					sumRow2.createCell(3).setCellStyle(dataStyle);
					rowCnt++;
					
					HSSFRow sumRow3 = sheet1.getRow(rowCnt);
					HSSFCell sumCell3 = sumRow3.createCell(2);
					sumCell3.setCellValue((double) addSubSum);
					sumCell3.setCellStyle(dataStyle);
					sumRow3.createCell(3).setCellStyle(dataStyle);
					rowCnt++;
					
					HSSFRow sumRow4 = sheet1.getRow(rowCnt);
					HSSFCell sumCell4 = sumRow4.createCell(3);
					sumCell4.setCellValue((double) wiSum);
					sumCell4.setCellStyle(dataStyle);
					rowCnt++;
					
					HSSFRow sumRow5 = sheet1.getRow(rowCnt);
					HSSFCell sumCell5 = sumRow5.createCell(3);
					sumCell5.setCellValue((double) haeSum);
					sumCell5.setCellStyle(dataStyle);

					bfMonSum = 0;
					thMonSum = 0;
					addSubSum = 0;
					wiSum = 0;
					haeSum = 0;
					rowCnt = nwRowCnt + 7;
					cellCnt = 4;
					addCellCnt = 0;
					
					HSSFRow typeRow = sheet1.createRow(rowCnt);
					HSSFCell typeCell = typeRow.createCell(0);
					typeCell.setCellValue(record.getInt("IND_TYPE") + 1 + "." + nwType);
					sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 1));
					typeCell.setCellStyle(mainStyle);
					
					rowCnt++;
					nwRowCnt = rowCnt;
				} else {
					rowCnt = nwRowCnt;
				}
			} else {
				HSSFRow typeRow = sheet1.createRow(nwRowCnt);
				HSSFCell typeCell = typeRow.createCell(0);
				typeCell.setCellValue(record.getInt("IND_TYPE") + 1 + "." + nwType);
				sheet1.addMergedRegion(new CellRangeAddress(nwRowCnt, nwRowCnt, 0, 1));
				typeCell.setCellStyle(mainStyle);
				
				rowCnt = nwRowCnt + 1;
				nwRowCnt = rowCnt;
			}

			if (sheet1.getRow(rowCnt) == null) {
				
				HSSFRow headRow = sheet1.createRow(rowCnt);
				HSSFCell headCell = headRow.createCell(1);
				HSSFCell sumCell = headRow.createCell(2);
				
				headCell.setCellValue("구분");
				sumCell.setCellValue("계");
				
				headCell.setCellStyle(headStyle);
				sumCell.setCellStyle(headStyle);
				headRow.createCell(3).setCellStyle(headStyle);
				
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 2, 3));
				
				HSSFCell orgCell = headRow.createCell(addCellCnt + cellCnt);
				orgCell.setCellValue(record.getString("ORG_NAME"));
				orgCell.setCellStyle(headStyle);
				rowCnt++;
				
				
				HSSFRow headRow2 = sheet1.createRow(rowCnt);
				HSSFCell headCell2 = headRow2.createCell(1);
				
				headCell2.setCellValue("전월");
				headCell2.setCellStyle(headStyle);
				
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 2, 3));
				
				HSSFCell cntCell = headRow2.createCell(addCellCnt + cellCnt);
				cntCell.setCellValue(record.getString("CNT1"));
				cntCell.setCellStyle(dataStyle);
				rowCnt++;
				
				
				HSSFRow headRow3 = sheet1.createRow(rowCnt);
				HSSFCell headCell3 = headRow3.createCell(1);
				
				headCell3.setCellValue("금월");
				headCell3.setCellStyle(headStyle);
				
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 2, 3));
				
				HSSFCell cntCell2 = headRow3.createCell(addCellCnt + cellCnt);
				cntCell2.setCellValue(record.getString("CNT2"));
				cntCell2.setCellStyle(dataStyle);
				rowCnt++;
				
				
				HSSFRow headRow4 = sheet1.createRow(rowCnt);
				HSSFCell headCell4 = headRow4.createCell(1);
				
				headCell4.setCellValue("증감");
				headCell4.setCellStyle(headStyle);
				
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 2, 3));
				HSSFCell cntCell3 = headRow4.createCell(addCellCnt + cellCnt);
				
				cntCell3.setCellValue(record.getString("CNT3"));
				cntCell3.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow headRow5 = sheet1.createRow(rowCnt);
				HSSFCell headCell5 = headRow5.createCell(1);
				HSSFCell headCell6 = headRow5.createCell(2);
				
				headCell5.setCellValue("비고");
				headCell6.setCellValue("위촉");
				
				headCell5.setCellStyle(headStyle);
				headCell6.setCellStyle(headStyle);
				
				HSSFCell cntCell4 = headRow5.createCell(addCellCnt + cellCnt);
				cntCell4.setCellValue(record.getString("CNT4"));
				cntCell4.setCellStyle(dataStyle);
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt + 1, 1, 1));
				rowCnt++;
				
				HSSFRow headRow6 = sheet1.createRow(rowCnt);
				HSSFCell headCell7 = headRow6.createCell(1);
				HSSFCell headCell8 = headRow6.createCell(2);
				
				headCell8.setCellValue("해촉");
				
				headCell7.setCellStyle(headStyle);
				headCell8.setCellStyle(headStyle);

				HSSFCell cntCell5 = headRow6.createCell(addCellCnt + cellCnt);
				cntCell5.setCellValue(record.getString("CNT5"));
				cntCell5.setCellStyle(dataStyle);
				
			} else {
				
				HSSFRow headRow = sheet1.getRow(rowCnt);
				HSSFCell orgCell = headRow.createCell(addCellCnt + cellCnt);
				orgCell.setCellValue(record.getString("ORG_NAME"));
				orgCell.setCellStyle(headStyle);
				rowCnt++;
				
				HSSFRow cntRow = sheet1.getRow(rowCnt);
				HSSFCell cntCell = cntRow.createCell(addCellCnt + cellCnt);
				cntCell.setCellValue(record.getString("CNT1"));
				cntCell.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow cntRow2 = sheet1.getRow(rowCnt);
				HSSFCell cntCell2 = cntRow2.createCell(addCellCnt + cellCnt);
				cntCell2.setCellValue(record.getString("CNT2"));
				cntCell2.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow cntRow3 = sheet1.getRow(rowCnt);
				HSSFCell cntCell3 = cntRow3.createCell(addCellCnt + cellCnt);
				cntCell3.setCellValue(record.getString("CNT3"));
				cntCell3.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow cntRow4 = sheet1.getRow(rowCnt);
				HSSFCell cntCell4 = cntRow4.createCell(addCellCnt + cellCnt);
				cntCell4.setCellValue(record.getString("CNT4"));
				cntCell4.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow cntRow5 = sheet1.getRow(rowCnt);
				HSSFCell cntCell5 = cntRow5.createCell(addCellCnt + cellCnt);
				cntCell5.setCellValue(record.getString("CNT5"));
				cntCell5.setCellStyle(dataStyle);
			}

			bfMonSum += record.getInt("CNT1");
			thMonSum += record.getInt("CNT2");
			addSubSum += record.getInt("CNT3");
			wiSum += record.getInt("CNT4");
			haeSum += record.getInt("CNT5");
			++addCellCnt;
			if (k == dataList.size() - 1) {
				rowCnt = nwRowCnt + 1;
				
				HSSFRow sumRow = sheet1.getRow(rowCnt);
				HSSFCell sumCell = sumRow.createCell(2);
				sumCell.setCellValue((double) bfMonSum);
				sumCell.setCellStyle(dataStyle);
				sumRow.createCell(3).setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow sumRow2 = sheet1.getRow(rowCnt);
				HSSFCell sumCell2 = sumRow2.createCell(2);
				sumCell2.setCellValue((double) thMonSum);
				sumCell2.setCellStyle(dataStyle);
				sumRow2.createCell(3).setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow sumRow3 = sheet1.getRow(rowCnt);
				HSSFCell sumCell3 = sumRow3.createCell(2);
				sumCell3.setCellValue((double) addSubSum);
				sumCell3.setCellStyle(dataStyle);
				sumRow3.createCell(3).setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow sumRow4 = sheet1.getRow(rowCnt);
				HSSFCell sumCell4 = sumRow4.createCell(3);
				sumCell4.setCellValue((double) wiSum);
				sumCell4.setCellStyle(dataStyle);
				rowCnt++;
				
				HSSFRow sumRow5 = sheet1.getRow(rowCnt);
				HSSFCell sumCell5 = sumRow5.createCell(3);
				sumCell5.setCellValue((double) haeSum);
				sumCell5.setCellStyle(dataStyle);
			}
		}

	}
    
	
	
	
	// 금일 접수 현황 엑셀 다운로드
    protected void receiptDown(Map model, HSSFWorkbook wb) {
    	CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
 	    CellStyle cutStyle = wb.createCellStyle();
 	    cutStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
 	    
        List dataList = (List) model.get("Data");
        HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
        
        HSSFRow titlerow = sheet1.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0);
        titleCell.setCellValue(model.get("titleName").toString());
        titleCell.setCellStyle(titleStyle);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));

        int rowCnt = 0 + 1;
        sheet1.createRow(rowCnt);
        int rowCnt2 = rowCnt + 1;
        HSSFRow headrow1 = sheet1.createRow(rowCnt2);
       
        HSSFCell headCell0 = headrow1.createCell(0);
        HSSFCell headCell1 = headrow1.createCell(1);
        HSSFCell headCell2 = headrow1.createCell(2);
        HSSFCell headCell3 = headrow1.createCell(3);
        HSSFCell headCell4 = headrow1.createCell(4);
        HSSFCell headCell5 = headrow1.createCell(5);
        HSSFCell headCell6 = headrow1.createCell(6);
        HSSFCell headCell7 = headrow1.createCell(7);
        HSSFCell headCell8 = headrow1.createCell(8);
        HSSFCell headCell9 = headrow1.createCell(9);
        HSSFCell headCell10 = headrow1.createCell(10);
        HSSFCell headCell11 = headrow1.createCell(11);
        HSSFCell headCell12 = headrow1.createCell(12);
        HSSFCell headCell13 = headrow1.createCell(13);

        headCell0.setCellValue("번호");
        headCell0.setCellStyle(headStyle);

        headCell1.setCellValue("일자");
        headCell1.setCellStyle(headStyle);

        headCell2.setCellValue("전송");
        headCell2.setCellStyle(headStyle);

        headCell3.setCellValue("방송");
        headCell3.setCellStyle(headStyle);

        headCell4.setCellValue("제보시간");
        headCell4.setCellStyle(headStyle);

        headCell5.setCellValue("방송시간");
        headCell5.setCellStyle(headStyle);

        headCell6.setCellValue("유형(대)");
        headCell6.setCellStyle(headStyle);

        headCell7.setCellValue("유형(중)");
        headCell7.setCellStyle(headStyle);

        headCell8.setCellValue("제보자");
        headCell8.setCellStyle(headStyle);

        headCell9.setCellValue("구분");
        headCell9.setCellStyle(headStyle);

        headCell10.setCellValue("접수자");
        headCell10.setCellStyle(headStyle);

        headCell11.setCellValue("내용");
        headCell11.setCellStyle(headStyle);

        headCell12.setCellValue("제보처");
        headCell12.setCellStyle(headStyle);

        headCell13.setCellValue("교통방송");
        headCell13.setCellStyle(headStyle);

        
        HSSFCell cutCell = headrow1.createCell(14);
        cutCell.setCellStyle(cutStyle);
        
        int rowCnt3 = rowCnt2 + 1;
        HSSFRow[] dataRow = new HSSFRow[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataRow[i] = sheet1.createRow(rowCnt3 + i);
            RecordDto record = (RecordDto) dataList.get(i);
            
            HSSFCell dataCell0 = dataRow[i].createCell(0);
            dataCell0.setCellValue(record.getString("RNUM"));
            dataCell0.setCellStyle(dataStyle);

            HSSFCell dataCell1 = dataRow[i].createCell(1);
            dataCell1.setCellValue(record.getString("RECEIPT_DAY"));
            dataCell1.setCellStyle(dataStyle);

            HSSFCell dataCell2 = dataRow[i].createCell(2);
            dataCell2.setCellValue(record.getString("FLAG_SEND"));
            dataCell2.setCellStyle(dataStyle);

            HSSFCell dataCell3 = dataRow[i].createCell(3);
            dataCell3.setCellValue(record.getString("FLAG_BROD"));
            dataCell3.setCellStyle(dataStyle);

            HSSFCell dataCell4 = dataRow[i].createCell(4);
            dataCell4.setCellValue(record.getString("RECEIPT_TIME"));
            dataCell4.setCellStyle(dataStyle);

            HSSFCell dataCell5 = dataRow[i].createCell(5);
            dataCell5.setCellValue(record.getString("BROAD_TIME"));
            dataCell5.setCellStyle(dataStyle);

            HSSFCell dataCell6 = dataRow[i].createCell(6);
            dataCell6.setCellValue(record.getString("REPORT_TYPE"));
            dataCell6.setCellStyle(dataStyle);

            HSSFCell dataCell7 = dataRow[i].createCell(7);
            dataCell7.setCellValue(record.getString("REPORT_TYPE2"));
            dataCell7.setCellStyle(dataStyle);

            HSSFCell dataCell8 = dataRow[i].createCell(8);
            dataCell8.setCellValue(record.getString("INDIVIDUAL_NAME"));
            dataCell8.setCellStyle(dataStyle);

            HSSFCell dataCell9 = dataRow[i].createCell(9);
            dataCell9.setCellValue(record.getString("TYPE_NAME"));
            dataCell9.setCellStyle(dataStyle);

            HSSFCell dataCell10 = dataRow[i].createCell(10);
            dataCell10.setCellValue(record.getString("RECEPTION_NAME"));
            dataCell10.setCellStyle(dataStyle);

            HSSFCell dataCell11 = dataRow[i].createCell(11);
            dataCell11.setCellValue(record.getString("MEMO"));
            dataCell11.setCellStyle(dataStyle);

            HSSFCell dataCell12 = dataRow[i].createCell(12);
            dataCell12.setCellValue(record.getString("REPORTER_TYPE"));
            dataCell12.setCellStyle(dataStyle);

            HSSFCell dataCell13 = dataRow[i].createCell(13);
            dataCell13.setCellValue(record.getString("REGION_NAME"));
            dataCell13.setCellStyle(dataStyle);

        }
        
        sheet1.setColumnWidth(0, 5000);
        sheet1.setColumnWidth(1, 5500);
        sheet1.setColumnWidth(2, 3000);
        sheet1.setColumnWidth(3, 3000);
        sheet1.setColumnWidth(4, 5500);
        sheet1.setColumnWidth(5, 6000);
        sheet1.setColumnWidth(6, 6000);
        sheet1.setColumnWidth(7, 6000);
        sheet1.setColumnWidth(8, 6000);
        sheet1.setColumnWidth(9, 5000);
        sheet1.setColumnWidth(10, 6000);
        sheet1.setColumnWidth(11, 6000);
        sheet1.setColumnWidth(12, 4000);
        sheet1.setColumnWidth(13, 6000);
        
    }

    
    
    
    // 통신원 소속별 일자별 제보건수
    protected void dayReceipt(Map model, HSSFWorkbook wb) {
        List dataList = (List) model.get("dayReceiptList");
        HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
        
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용


 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		DataFormat format = wb.createDataFormat();

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));
		
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    
        HSSFRow titlerow = sheet1.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0);
        titleCell.setCellValue(model.get("titleName").toString());
        titleCell.setCellStyle(titleStyle);
        
        int rowCnt = 0 + 1;
        HSSFRow headrow0 = sheet1.createRow(rowCnt);
        HSSFCell sendCell = headrow0.createCell(0);
        sendCell.setCellValue("건수 : " + dataList.size());
        sendCell.setCellStyle(mainStyle);
        headrow0.createCell(1).setCellStyle(mainStyle);
        /*headrow0.createCell(1).setCellValue(dataList.size());*/

        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 37)); //제목 셀 병합
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 1)); //건수 셀 병합
        
        sheet1.setColumnWidth(0, 5000);
        sheet1.setColumnWidth(2, 6000);
        sheet1.setColumnWidth(3, 6000);
        sheet1.setColumnWidth(4, 6000);
        sheet1.setColumnWidth(5, 6000);

        int rowCnt2 = rowCnt + 1;
        HSSFRow headrow1 = sheet1.createRow(rowCnt2);
        
        HSSFCell headCell0 = headrow1.createCell(0);
        HSSFCell headCell1 = headrow1.createCell(1);
        HSSFCell headCell2 = headrow1.createCell(2);
        HSSFCell headCell3 = headrow1.createCell(3);
        HSSFCell headCell4 = headrow1.createCell(4);
        HSSFCell headCell5 = headrow1.createCell(5);
        HSSFCell headCell6 = headrow1.createCell(6);
        HSSFCell headCell7 = headrow1.createCell(7);
        HSSFCell headCell8 = headrow1.createCell(8);
        HSSFCell headCell9 = headrow1.createCell(9);
        HSSFCell headCell10 = headrow1.createCell(10);
        HSSFCell headCell11 = headrow1.createCell(11);
        HSSFCell headCell12 = headrow1.createCell(12);
        HSSFCell headCell13 = headrow1.createCell(13);
        HSSFCell headCell14 = headrow1.createCell(14);
        HSSFCell headCell15 = headrow1.createCell(15);
        HSSFCell headCell16 = headrow1.createCell(16);
        HSSFCell headCell17 = headrow1.createCell(17);
        HSSFCell headCell18 = headrow1.createCell(18);
        HSSFCell headCell19 = headrow1.createCell(19);
        HSSFCell headCell20 = headrow1.createCell(20);
        HSSFCell headCell21 = headrow1.createCell(21);
        HSSFCell headCell22 = headrow1.createCell(22);
        HSSFCell headCell23 = headrow1.createCell(23);
        HSSFCell headCell24 = headrow1.createCell(24);
        HSSFCell headCell25 = headrow1.createCell(25);
        HSSFCell headCell26 = headrow1.createCell(26);
        HSSFCell headCell27 = headrow1.createCell(27);
        HSSFCell headCell28 = headrow1.createCell(28);
        HSSFCell headCell29 = headrow1.createCell(29);
        HSSFCell headCell30 = headrow1.createCell(30);
        HSSFCell headCell31 = headrow1.createCell(31);
        HSSFCell headCell32 = headrow1.createCell(32);
        HSSFCell headCell33 = headrow1.createCell(33);
        HSSFCell headCell34 = headrow1.createCell(34);
        HSSFCell headCell35 = headrow1.createCell(35);
        HSSFCell headCell36 = headrow1.createCell(36);
        HSSFCell headCell37 = headrow1.createCell(37);

        
        headCell0.setCellValue("연번");
        headCell1.setCellValue("ID");
        headCell2.setCellValue("성명");
        headCell3.setCellValue("전화번호");
        headCell4.setCellValue("기관명");
        headCell5.setCellValue("생일");
        headCell6.setCellValue("월계");
        headCell7.setCellValue("1");
        headCell8.setCellValue("2");
        headCell9.setCellValue("3");
        headCell10.setCellValue("4");
        headCell11.setCellValue("5");
        headCell12.setCellValue("6");
        headCell13.setCellValue("7");
        headCell14.setCellValue("8");
        headCell15.setCellValue("9");
        headCell16.setCellValue("10");
        headCell17.setCellValue("11");
        headCell18.setCellValue("12");
        headCell19.setCellValue("13");
        headCell20.setCellValue("14");
        headCell21.setCellValue("15");
        headCell22.setCellValue("16");
        headCell23.setCellValue("17");
        headCell24.setCellValue("18");
        headCell25.setCellValue("19");
        headCell26.setCellValue("20");
        headCell27.setCellValue("21");
        headCell28.setCellValue("22");
        headCell29.setCellValue("23");
        headCell30.setCellValue("24");
        headCell31.setCellValue("25");
        headCell32.setCellValue("26");
        headCell33.setCellValue("27");
        headCell34.setCellValue("28");
        headCell35.setCellValue("29");
        headCell36.setCellValue("30");
        headCell37.setCellValue("31");
        
        
        headCell0.setCellStyle(headStyle);
        headCell1.setCellStyle(headStyle);
        headCell2.setCellStyle(headStyle);
        headCell3.setCellStyle(headStyle);
        headCell4.setCellStyle(headStyle);
        headCell5.setCellStyle(headStyle);
        headCell6.setCellStyle(headStyle);
        headCell7.setCellStyle(headStyle);
        headCell8.setCellStyle(headStyle);
        headCell9.setCellStyle(headStyle);
        headCell10.setCellStyle(headStyle);
        headCell11.setCellStyle(headStyle);
        headCell12.setCellStyle(headStyle);
        headCell13.setCellStyle(headStyle);
        headCell14.setCellStyle(headStyle);
        headCell15.setCellStyle(headStyle);
        headCell16.setCellStyle(headStyle);
        headCell17.setCellStyle(headStyle);
        headCell18.setCellStyle(headStyle);
        headCell19.setCellStyle(headStyle);
        headCell20.setCellStyle(headStyle);
        headCell21.setCellStyle(headStyle);
        headCell22.setCellStyle(headStyle);
        headCell23.setCellStyle(headStyle);
        headCell24.setCellStyle(headStyle);
        headCell25.setCellStyle(headStyle);
        headCell26.setCellStyle(headStyle);
        headCell27.setCellStyle(headStyle);
        headCell28.setCellStyle(headStyle);
        headCell29.setCellStyle(headStyle);
        headCell30.setCellStyle(headStyle);
        headCell31.setCellStyle(headStyle);
        headCell32.setCellStyle(headStyle);
        headCell33.setCellStyle(headStyle);
        headCell34.setCellStyle(headStyle);
        headCell35.setCellStyle(headStyle);
        headCell36.setCellStyle(headStyle);
        headCell37.setCellStyle(headStyle);

        

        int rowCnt3 = rowCnt2 + 1;
        HSSFRow[] dataRow = new HSSFRow[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataRow[i] = sheet1.createRow(rowCnt3 + i);
            RecordDto record = (RecordDto) dataList.get(i);
            
            HSSFCell dataCell0 = dataRow[i].createCell(0);
            dataCell0.setCellValue(i + 1);
            dataCell0.setCellStyle(dataStyle);

            HSSFCell dataCell1 = dataRow[i].createCell(1);
            dataCell1.setCellValue(record.getString("ACT_ID"));
            dataCell1.setCellStyle(dataStyle);

            HSSFCell dataCell2 = dataRow[i].createCell(2);
            dataCell2.setCellValue(record.getString("INFORMER_NAME"));
            dataCell2.setCellStyle(dataStyle);

            HSSFCell dataCell3 = dataRow[i].createCell(3);
            dataCell3.setCellValue(record.getString("PHONE_CELL"));
            dataCell3.setCellStyle(dataStyle);

            HSSFCell dataCell4 = dataRow[i].createCell(4);
            dataCell4.setCellValue(record.getString("ORG_NAME"));
            dataCell4.setCellStyle(dataStyle);

            HSSFCell dataCell5 = dataRow[i].createCell(5);
            dataCell5.setCellValue(record.getString("BIRTHDAY"));
            dataCell5.setCellStyle(dataStyle);

            HSSFCell dataCell6 = dataRow[i].createCell(6);
            dataCell6.setCellValue(record.getString("SUM_CNT") != null && !record.getString("SUM_CNT").isEmpty() ? Integer.parseInt(record.getString("SUM_CNT")) : 0);
            dataCell6.setCellStyle(dataStyle);

            HSSFCell dataCell7 = dataRow[i].createCell(7);
            dataCell7.setCellValue(record.getString("D1") != null && !record.getString("D1").isEmpty() ? Integer.parseInt(record.getString("D1")) : 0);
            dataCell7.setCellStyle(dataStyle);

            HSSFCell dataCell8 = dataRow[i].createCell(8);
            dataCell8.setCellValue(record.getString("D2") != null && !record.getString("D2").isEmpty() ? Integer.parseInt(record.getString("D2")) : 0);
            dataCell8.setCellStyle(dataStyle);

            HSSFCell dataCell9 = dataRow[i].createCell(9);
            dataCell9.setCellValue(record.getString("D3") != null && !record.getString("D3").isEmpty() ? Integer.parseInt(record.getString("D3")) : 0);
            dataCell9.setCellStyle(dataStyle);

            HSSFCell dataCell10 = dataRow[i].createCell(10);
            dataCell10.setCellValue(record.getString("D4") != null && !record.getString("D4").isEmpty() ? Integer.parseInt(record.getString("D4")) : 0);
            dataCell10.setCellStyle(dataStyle);

            HSSFCell dataCell11 = dataRow[i].createCell(11);
            dataCell11.setCellValue(record.getString("D5") != null && !record.getString("D5").isEmpty() ? Integer.parseInt(record.getString("D5")) : 0);
            dataCell11.setCellStyle(dataStyle);

            HSSFCell dataCell12 = dataRow[i].createCell(12);
            dataCell12.setCellValue(record.getString("D6") != null && !record.getString("D6").isEmpty() ? Integer.parseInt(record.getString("D6")) : 0);
            dataCell12.setCellStyle(dataStyle);

            HSSFCell dataCell13 = dataRow[i].createCell(13);
            dataCell13.setCellValue(record.getString("D7") != null && !record.getString("D7").isEmpty() ? Integer.parseInt(record.getString("D7")) : 0);
            dataCell13.setCellStyle(dataStyle);

            HSSFCell dataCell14 = dataRow[i].createCell(14);
            dataCell14.setCellValue(record.getString("D8") != null && !record.getString("D8").isEmpty() ? Integer.parseInt(record.getString("D8")) : 0);
            dataCell14.setCellStyle(dataStyle);

            HSSFCell dataCell15 = dataRow[i].createCell(15);
            dataCell15.setCellValue(record.getString("D9") != null && !record.getString("D9").isEmpty() ? Integer.parseInt(record.getString("D9")) : 0);
            dataCell15.setCellStyle(dataStyle);

            HSSFCell dataCell16 = dataRow[i].createCell(16);
            dataCell16.setCellValue(record.getString("D10") != null && !record.getString("D10").isEmpty() ? Integer.parseInt(record.getString("D10")) : 0);
            dataCell16.setCellStyle(dataStyle);

            HSSFCell dataCell17 = dataRow[i].createCell(17);
            dataCell17.setCellValue(record.getString("D11") != null && !record.getString("D11").isEmpty() ? Integer.parseInt(record.getString("D11")) : 0);
            dataCell17.setCellStyle(dataStyle);

            HSSFCell dataCell18 = dataRow[i].createCell(18);
            dataCell18.setCellValue(record.getString("D12") != null && !record.getString("D12").isEmpty() ? Integer.parseInt(record.getString("D12")) : 0);
            dataCell18.setCellStyle(dataStyle);

            HSSFCell dataCell19 = dataRow[i].createCell(19);
            dataCell19.setCellValue(record.getString("D13") != null && !record.getString("D13").isEmpty() ? Integer.parseInt(record.getString("D13")) : 0);
            dataCell19.setCellStyle(dataStyle);

            HSSFCell dataCell20 = dataRow[i].createCell(20);
            dataCell20.setCellValue(record.getString("D14") != null && !record.getString("D14").isEmpty() ? Integer.parseInt(record.getString("D14")) : 0);
            dataCell20.setCellStyle(dataStyle);

            HSSFCell dataCell21 = dataRow[i].createCell(21);
            dataCell21.setCellValue(record.getString("D15") != null && !record.getString("D15").isEmpty() ? Integer.parseInt(record.getString("D15")) : 0);
            dataCell21.setCellStyle(dataStyle);

            HSSFCell dataCell22 = dataRow[i].createCell(22);
            dataCell22.setCellValue(record.getString("D16") != null && !record.getString("D16").isEmpty() ? Integer.parseInt(record.getString("D16")) : 0);
            dataCell22.setCellStyle(dataStyle);

            HSSFCell dataCell23 = dataRow[i].createCell(23);
            dataCell23.setCellValue(record.getString("D17") != null && !record.getString("D17").isEmpty() ? Integer.parseInt(record.getString("D17")) : 0);
            dataCell23.setCellStyle(dataStyle);

            HSSFCell dataCell24 = dataRow[i].createCell(24);
            dataCell24.setCellValue(record.getString("D18") != null && !record.getString("D18").isEmpty() ? Integer.parseInt(record.getString("D18")) : 0);
            dataCell24.setCellStyle(dataStyle);

            HSSFCell dataCell25 = dataRow[i].createCell(25);
            dataCell25.setCellValue(record.getString("D19") != null && !record.getString("D19").isEmpty() ? Integer.parseInt(record.getString("D19")) : 0);
            dataCell25.setCellStyle(dataStyle);

            HSSFCell dataCell26 = dataRow[i].createCell(26);
            dataCell26.setCellValue(record.getString("D20") != null && !record.getString("D20").isEmpty() ? Integer.parseInt(record.getString("D20")) : 0);
            dataCell26.setCellStyle(dataStyle);

            HSSFCell dataCell27 = dataRow[i].createCell(27);
            dataCell27.setCellValue(record.getString("D21") != null && !record.getString("D21").isEmpty() ? Integer.parseInt(record.getString("D21")) : 0);
            dataCell27.setCellStyle(dataStyle);

            HSSFCell dataCell28 = dataRow[i].createCell(28);
            dataCell28.setCellValue(record.getString("D22") != null && !record.getString("D22").isEmpty() ? Integer.parseInt(record.getString("D22")) : 0);
            dataCell28.setCellStyle(dataStyle);
            
            HSSFCell dataCell29 = dataRow[i].createCell(29);
            dataCell29.setCellValue(record.getString("D23") != null && !record.getString("D23").isEmpty() ? Integer.parseInt(record.getString("D23")) : 0);
            dataCell29.setCellStyle(dataStyle);
            
            HSSFCell dataCell30 = dataRow[i].createCell(30);
            dataCell30.setCellValue(record.getString("D24") != null && !record.getString("D24").isEmpty() ? Integer.parseInt(record.getString("D24")) : 0);
            dataCell30.setCellStyle(dataStyle);

            HSSFCell dataCell31 = dataRow[i].createCell(31);
            dataCell31.setCellValue(record.getString("D25") != null && !record.getString("D25").isEmpty() ? Integer.parseInt(record.getString("D25")) : 0);
            dataCell31.setCellStyle(dataStyle);
            
            HSSFCell dataCell32 = dataRow[i].createCell(32);
            dataCell32.setCellValue(record.getString("D26") != null && !record.getString("D26").isEmpty() ? Integer.parseInt(record.getString("D26")) : 0);
            dataCell32.setCellStyle(dataStyle);

            HSSFCell dataCell33 = dataRow[i].createCell(33);
            dataCell33.setCellValue(record.getString("D27") != null && !record.getString("D27").isEmpty() ? Integer.parseInt(record.getString("D27")) : 0);
            dataCell33.setCellStyle(dataStyle);
            
            HSSFCell dataCell34 = dataRow[i].createCell(34);
            dataCell34.setCellValue(record.getString("D28") != null && !record.getString("D28").isEmpty() ? Integer.parseInt(record.getString("D28")) : 0);
            dataCell34.setCellStyle(dataStyle);
            
            HSSFCell dataCell35 = dataRow[i].createCell(35);
            dataCell35.setCellValue(record.getString("D29") != null && !record.getString("D29").isEmpty() ? Integer.parseInt(record.getString("D29")) : 0);
            dataCell35.setCellStyle(dataStyle);
            
            HSSFCell dataCell36 = dataRow[i].createCell(36);
            dataCell36.setCellValue(record.getString("D30") != null && !record.getString("D30").isEmpty() ? Integer.parseInt(record.getString("D30")) : 0);
            dataCell36.setCellStyle(dataStyle);
            
            HSSFCell dataCell37 = dataRow[i].createCell(37);
            dataCell37.setCellValue(record.getString("D31") != null && !record.getString("D31").isEmpty() ? Integer.parseInt(record.getString("D31")) : 0);
            dataCell37.setCellStyle(dataStyle);
        }
    }

    
    
    
    // 사회봉사자 일자별 통계
    protected void volunteer(Map model, HSSFWorkbook wb) {
        List dataList = (List) model.get("vltList");
        HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
        
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용


 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		DataFormat format = wb.createDataFormat();

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		dataStyle.setDataFormat(format.getFormat("#,##0"));
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
        HSSFRow titlerow = sheet1.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0);
        titleCell.setCellValue(model.get("titleName").toString());
        titleCell.setCellStyle(titleStyle);
        
        int rowCnt = 0 + 1;
        HSSFRow headrow0 = sheet1.createRow(rowCnt);
        HSSFCell sendCell = headrow0.createCell(0);
        sendCell.setCellValue("건수 : " + dataList.size());
        sendCell.setCellStyle(mainStyle);
        
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 37)); //제목 셀 병합
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 1)); //건수 셀 병합
        
        sheet1.setColumnWidth(0, 5000);
        sheet1.setColumnWidth(2, 6000);
        sheet1.setColumnWidth(3, 6000);
        sheet1.setColumnWidth(4, 6000);
        sheet1.setColumnWidth(5, 6000);
        
        int rowCnt2 = rowCnt + 1;
        HSSFRow headrow1 = sheet1.createRow(rowCnt2);
        
        HSSFCell headCell0 = headrow1.createCell(0);
        HSSFCell headCell1 = headrow1.createCell(1);
        HSSFCell headCell2 = headrow1.createCell(2);
        HSSFCell headCell3 = headrow1.createCell(3);
        HSSFCell headCell4 = headrow1.createCell(4);
        HSSFCell headCell5 = headrow1.createCell(5); 
        HSSFCell headCell6 = headrow1.createCell(6);
        HSSFCell headCell7 = headrow1.createCell(7);
        HSSFCell headCell8 = headrow1.createCell(8);
        HSSFCell headCell9 = headrow1.createCell(9);
        HSSFCell headCell10 = headrow1.createCell(10);
        HSSFCell headCell11 = headrow1.createCell(11);
        HSSFCell headCell12 = headrow1.createCell(12);
        HSSFCell headCell13 = headrow1.createCell(13);
        HSSFCell headCell14 = headrow1.createCell(14);
        HSSFCell headCell15 = headrow1.createCell(15);
        HSSFCell headCell16 = headrow1.createCell(16);
        HSSFCell headCell17 = headrow1.createCell(17);
        HSSFCell headCell18 = headrow1.createCell(18);
        HSSFCell headCell19 = headrow1.createCell(19);
        HSSFCell headCell20 = headrow1.createCell(20);
        HSSFCell headCell21 = headrow1.createCell(21);
        HSSFCell headCell22 = headrow1.createCell(22);
        HSSFCell headCell23 = headrow1.createCell(23);
        HSSFCell headCell24 = headrow1.createCell(24);
        HSSFCell headCell25 = headrow1.createCell(25);
        HSSFCell headCell26 = headrow1.createCell(26);
        HSSFCell headCell27 = headrow1.createCell(27);
        HSSFCell headCell28 = headrow1.createCell(28);
        HSSFCell headCell29 = headrow1.createCell(29);
        HSSFCell headCell30 = headrow1.createCell(30);
        HSSFCell headCell31 = headrow1.createCell(31);
        HSSFCell headCell32 = headrow1.createCell(32);
        HSSFCell headCell33 = headrow1.createCell(33);
        HSSFCell headCell34 = headrow1.createCell(34);
        HSSFCell headCell35 = headrow1.createCell(35);
        HSSFCell headCell36 = headrow1.createCell(36);
        HSSFCell headCell37 = headrow1.createCell(37);

        
        headCell0.setCellValue("연번");
        headCell1.setCellValue("ID");
        headCell2.setCellValue("성명");
        headCell3.setCellValue("전화번호");
        headCell4.setCellValue("기관명");
        headCell5.setCellValue("생일");
        headCell6.setCellValue("월계");
        headCell7.setCellValue("1");
        headCell8.setCellValue("2");
        headCell9.setCellValue("3");
        headCell10.setCellValue("4");
        headCell11.setCellValue("5");
        headCell12.setCellValue("6");
        headCell13.setCellValue("7");
        headCell14.setCellValue("8");
        headCell15.setCellValue("9");
        headCell16.setCellValue("10");
        headCell17.setCellValue("11");
        headCell18.setCellValue("12");
        headCell19.setCellValue("13");
        headCell20.setCellValue("14");
        headCell21.setCellValue("15"); 
        headCell22.setCellValue("16");
        headCell23.setCellValue("17");
        headCell24.setCellValue("18");
        headCell25.setCellValue("19");
        headCell26.setCellValue("20");
        headCell27.setCellValue("21");
        headCell28.setCellValue("22");
        headCell29.setCellValue("23");
        headCell30.setCellValue("24");
        headCell31.setCellValue("25");
        headCell32.setCellValue("26");
        headCell33.setCellValue("27");
        headCell34.setCellValue("28");
        headCell35.setCellValue("29");
        headCell36.setCellValue("30");
        headCell37.setCellValue("31");
        
        
        headCell0.setCellStyle(headStyle);
        headCell1.setCellStyle(headStyle);
        headCell2.setCellStyle(headStyle);
        headCell3.setCellStyle(headStyle);
        headCell4.setCellStyle(headStyle);
        headCell5.setCellStyle(headStyle);
        headCell6.setCellStyle(headStyle);
        headCell7.setCellStyle(headStyle);
        headCell8.setCellStyle(headStyle);
        headCell9.setCellStyle(headStyle);
        headCell10.setCellStyle(headStyle);
        headCell11.setCellStyle(headStyle);
        headCell12.setCellStyle(headStyle);
        headCell13.setCellStyle(headStyle);
        headCell14.setCellStyle(headStyle);
        headCell15.setCellStyle(headStyle);
        headCell16.setCellStyle(headStyle);
        headCell17.setCellStyle(headStyle);
        headCell18.setCellStyle(headStyle);
        headCell19.setCellStyle(headStyle);
        headCell20.setCellStyle(headStyle);
        headCell21.setCellStyle(headStyle);
        headCell22.setCellStyle(headStyle);
        headCell23.setCellStyle(headStyle);
        headCell24.setCellStyle(headStyle);
        headCell25.setCellStyle(headStyle);
        headCell26.setCellStyle(headStyle);
        headCell27.setCellStyle(headStyle);
        headCell28.setCellStyle(headStyle);
        headCell29.setCellStyle(headStyle);
        headCell30.setCellStyle(headStyle);
        headCell31.setCellStyle(headStyle);
        headCell32.setCellStyle(headStyle);
        headCell33.setCellStyle(headStyle);
        headCell34.setCellStyle(headStyle);
        headCell35.setCellStyle(headStyle);
        headCell36.setCellStyle(headStyle);
        headCell37.setCellStyle(headStyle);
        
        int rowCnt3 = rowCnt2 + 1;
        HSSFRow[] dataRow = new HSSFRow[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataRow[i] = sheet1.createRow(rowCnt3 + i);
            RecordDto record = (RecordDto) dataList.get(i);
            HSSFCell dataCell0 = dataRow[i].createCell(0);
            dataCell0.setCellValue(i + 1);
            dataCell0.setCellStyle(dataStyle);

            HSSFCell dataCell1 = dataRow[i].createCell(1);
            dataCell1.setCellValue(record.getString("ACT_ID"));
            dataCell1.setCellStyle(dataStyle);

            HSSFCell dataCell2 = dataRow[i].createCell(2);
            dataCell2.setCellValue(record.getString("INFORMER_NAME"));
            dataCell2.setCellStyle(dataStyle);

            HSSFCell dataCell3 = dataRow[i].createCell(3);
            dataCell3.setCellValue(record.getString("PHONE_CELL"));
            dataCell3.setCellStyle(dataStyle);

            HSSFCell dataCell4 = dataRow[i].createCell(4);
            dataCell4.setCellValue(record.getString("ORG_NAME"));
            dataCell4.setCellStyle(dataStyle);

            HSSFCell dataCell5 = dataRow[i].createCell(5);
            dataCell5.setCellValue(record.getString("BIRTHDAY"));
            dataCell5.setCellStyle(dataStyle);

            HSSFCell dataCell6 = dataRow[i].createCell(6);
            dataCell6.setCellValue(record.getString("SUM_CNT") != null && !record.getString("SUM_CNT").isEmpty() ? Integer.parseInt(record.getString("SUM_CNT")) : 0);
            dataCell6.setCellStyle(dataStyle);

            HSSFCell dataCell7 = dataRow[i].createCell(7);
            dataCell7.setCellValue(record.getString("D1") != null && !record.getString("D1").isEmpty() ? Integer.parseInt(record.getString("D1")) : 0);
            dataCell7.setCellStyle(dataStyle);

            HSSFCell dataCell8 = dataRow[i].createCell(8);
            dataCell8.setCellValue(record.getString("D2") != null && !record.getString("D2").isEmpty() ? Integer.parseInt(record.getString("D2")) : 0);
            dataCell8.setCellStyle(dataStyle);

            HSSFCell dataCell9 = dataRow[i].createCell(9);
            dataCell9.setCellValue(record.getString("D3") != null && !record.getString("D3").isEmpty() ? Integer.parseInt(record.getString("D3")) : 0);
            dataCell9.setCellStyle(dataStyle);

            HSSFCell dataCell10 = dataRow[i].createCell(10);
            dataCell10.setCellValue(record.getString("D4") != null && !record.getString("D4").isEmpty() ? Integer.parseInt(record.getString("D4")) : 0);
            dataCell10.setCellStyle(dataStyle);

            HSSFCell dataCell11 = dataRow[i].createCell(11);
            dataCell11.setCellValue(record.getString("D5") != null && !record.getString("D5").isEmpty() ? Integer.parseInt(record.getString("D5")) : 0);
            dataCell11.setCellStyle(dataStyle);

            HSSFCell dataCell12 = dataRow[i].createCell(12);
            dataCell12.setCellValue(record.getString("D6") != null && !record.getString("D6").isEmpty() ? Integer.parseInt(record.getString("D6")) : 0);
            dataCell12.setCellStyle(dataStyle);

            HSSFCell dataCell13 = dataRow[i].createCell(13);
            dataCell13.setCellValue(record.getString("D7") != null && !record.getString("D7").isEmpty() ? Integer.parseInt(record.getString("D7")) : 0);
            dataCell13.setCellStyle(dataStyle);

            HSSFCell dataCell14 = dataRow[i].createCell(14);
            dataCell14.setCellValue(record.getString("D8") != null && !record.getString("D8").isEmpty() ? Integer.parseInt(record.getString("D8")) : 0);
            dataCell14.setCellStyle(dataStyle);

            HSSFCell dataCell15 = dataRow[i].createCell(15);
            dataCell15.setCellValue(record.getString("D9") != null && !record.getString("D9").isEmpty() ? Integer.parseInt(record.getString("D9")) : 0);
            dataCell15.setCellStyle(dataStyle);

            HSSFCell dataCell16 = dataRow[i].createCell(16);
            dataCell16.setCellValue(record.getString("D10") != null && !record.getString("D10").isEmpty() ? Integer.parseInt(record.getString("D10")) : 0);
            dataCell16.setCellStyle(dataStyle);

            HSSFCell dataCell17 = dataRow[i].createCell(17);
            dataCell17.setCellValue(record.getString("D11") != null && !record.getString("D11").isEmpty() ? Integer.parseInt(record.getString("D11")) : 0);
            dataCell17.setCellStyle(dataStyle);

            HSSFCell dataCell18 = dataRow[i].createCell(18);
            dataCell18.setCellValue(record.getString("D12") != null && !record.getString("D12").isEmpty() ? Integer.parseInt(record.getString("D12")) : 0);
            dataCell18.setCellStyle(dataStyle);

            HSSFCell dataCell19 = dataRow[i].createCell(19);
            dataCell19.setCellValue(record.getString("D13") != null && !record.getString("D13").isEmpty() ? Integer.parseInt(record.getString("D13")) : 0);
            dataCell19.setCellStyle(dataStyle);

            HSSFCell dataCell20 = dataRow[i].createCell(20);
            dataCell20.setCellValue(record.getString("D14") != null && !record.getString("D14").isEmpty() ? Integer.parseInt(record.getString("D14")) : 0);
            dataCell20.setCellStyle(dataStyle);

            HSSFCell dataCell21 = dataRow[i].createCell(21);
            dataCell21.setCellValue(record.getString("D15") != null && !record.getString("D15").isEmpty() ? Integer.parseInt(record.getString("D15")) : 0);
            dataCell21.setCellStyle(dataStyle);

            HSSFCell dataCell22 = dataRow[i].createCell(22);
            dataCell22.setCellValue(record.getString("D16") != null && !record.getString("D16").isEmpty() ? Integer.parseInt(record.getString("D16")) : 0);
            dataCell22.setCellStyle(dataStyle);

            HSSFCell dataCell23 = dataRow[i].createCell(23);
            dataCell23.setCellValue(record.getString("D17") != null && !record.getString("D17").isEmpty() ? Integer.parseInt(record.getString("D17")) : 0);
            dataCell23.setCellStyle(dataStyle);

            HSSFCell dataCell24 = dataRow[i].createCell(24);
            dataCell24.setCellValue(record.getString("D18") != null && !record.getString("D18").isEmpty() ? Integer.parseInt(record.getString("D18")) : 0);
            dataCell24.setCellStyle(dataStyle);

            HSSFCell dataCell25 = dataRow[i].createCell(25);
            dataCell25.setCellValue(record.getString("D19") != null && !record.getString("D19").isEmpty() ? Integer.parseInt(record.getString("D19")) : 0);
            dataCell25.setCellStyle(dataStyle);

            HSSFCell dataCell26 = dataRow[i].createCell(26);
            dataCell26.setCellValue(record.getString("D20") != null && !record.getString("D20").isEmpty() ? Integer.parseInt(record.getString("D20")) : 0);
            dataCell26.setCellStyle(dataStyle);

            HSSFCell dataCell27 = dataRow[i].createCell(27);
            dataCell27.setCellValue(record.getString("D21") != null && !record.getString("D21").isEmpty() ? Integer.parseInt(record.getString("D21")) : 0);
            dataCell27.setCellStyle(dataStyle);

            HSSFCell dataCell28 = dataRow[i].createCell(28);
            dataCell28.setCellValue(record.getString("D22") != null && !record.getString("D22").isEmpty() ? Integer.parseInt(record.getString("D22")) : 0);
            dataCell28.setCellStyle(dataStyle);
            
            HSSFCell dataCell29 = dataRow[i].createCell(29);
            dataCell29.setCellValue(record.getString("D23") != null && !record.getString("D23").isEmpty() ? Integer.parseInt(record.getString("D23")) : 0);
            dataCell29.setCellStyle(dataStyle);
            
            HSSFCell dataCell30 = dataRow[i].createCell(30);
            dataCell30.setCellValue(record.getString("D24") != null && !record.getString("D24").isEmpty() ? Integer.parseInt(record.getString("D24")) : 0);
            dataCell30.setCellStyle(dataStyle);

            HSSFCell dataCell31 = dataRow[i].createCell(31);
            dataCell31.setCellValue(record.getString("D25") != null && !record.getString("D25").isEmpty() ? Integer.parseInt(record.getString("D25")) : 0);
            dataCell31.setCellStyle(dataStyle);
            
            HSSFCell dataCell32 = dataRow[i].createCell(32);
            dataCell32.setCellValue(record.getString("D26") != null && !record.getString("D26").isEmpty() ? Integer.parseInt(record.getString("D26")) : 0);
            dataCell32.setCellStyle(dataStyle);

            HSSFCell dataCell33 = dataRow[i].createCell(33);
            dataCell33.setCellValue(record.getString("D27") != null && !record.getString("D27").isEmpty() ? Integer.parseInt(record.getString("D27")) : 0);
            dataCell33.setCellStyle(dataStyle);
            
            HSSFCell dataCell34 = dataRow[i].createCell(34);
            dataCell34.setCellValue(record.getString("D28") != null && !record.getString("D28").isEmpty() ? Integer.parseInt(record.getString("D28")) : 0);
            dataCell34.setCellStyle(dataStyle);
            
            HSSFCell dataCell35 = dataRow[i].createCell(35);
            dataCell35.setCellValue(record.getString("D29") != null && !record.getString("D29").isEmpty() ? Integer.parseInt(record.getString("D29")) : 0);
            dataCell35.setCellStyle(dataStyle);
            
            HSSFCell dataCell36 = dataRow[i].createCell(36);
            dataCell36.setCellValue(record.getString("D30") != null && !record.getString("D30").isEmpty() ? Integer.parseInt(record.getString("D30")) : 0);
            dataCell36.setCellStyle(dataStyle);
            
            HSSFCell dataCell37 = dataRow[i].createCell(37);
            dataCell37.setCellValue(record.getString("D31") != null && !record.getString("D31").isEmpty() ? Integer.parseInt(record.getString("D31")) : 0);
            dataCell37.setCellStyle(dataStyle);
        }
    }

    
    // 한국가스기술공사 제보실적
    protected void korLx(Map model, HSSFWorkbook wb) {
		int rowCnt = 0;
		int gl = 0;
		List dataList = (List) model.get("Data");
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	   CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	    
	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

	    CellStyle headStyle = wb.createCellStyle();
    	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
    	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
    	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
    	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
    	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    
	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

	    
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		HSSFCell titleCell = titlerow.createCell(rowCnt);
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		rowCnt++;
		
		HSSFRow headrow = sheet1.createRow(rowCnt);
		HSSFCell mainCell = headrow.createCell(0);
		mainCell.setCellValue("실적날짜");
		mainCell.setCellStyle(mainStyle);
		rowCnt++;
		
		HSSFRow headrow2 = sheet1.createRow(rowCnt);
		if (model.get("sheetNames1").toString().equals("가스기술공사 제보실적")) {
			HSSFCell dataCell = headrow2.createCell(0);
			HSSFCell dataCell2 = headrow2.createCell(1);
			HSSFCell dataCell3 = headrow2.createCell(2);
			HSSFCell dataCell4 = headrow2.createCell(3);
			
			dataCell.setCellValue("연번");
			dataCell2.setCellValue("ID");
			dataCell3.setCellValue("이 름");
			dataCell4.setCellValue("제공건수");
			
			dataCell.setCellStyle(headStyle);
			dataCell2.setCellStyle(headStyle);
			dataCell3.setCellStyle(headStyle);
			dataCell4.setCellStyle(headStyle);
			
			gl = 3;
		} else {
			HSSFCell dataCell = headrow2.createCell(0);
			HSSFCell dataCell2 = headrow2.createCell(1);
			HSSFCell dataCell3 = headrow2.createCell(2);
			HSSFCell dataCell4 = headrow2.createCell(3);
			HSSFCell dataCell5 = headrow2.createCell(4);
			
			dataCell.setCellValue("순번");
			dataCell2.setCellValue("ID");
			dataCell3.setCellValue("성 명");
			dataCell4.setCellValue("전화번호");
			dataCell5.setCellValue("제공건수");
			
			dataCell.setCellStyle(headStyle);
			dataCell2.setCellStyle(headStyle);
			dataCell3.setCellStyle(headStyle);
			dataCell4.setCellStyle(headStyle);
			dataCell5.setCellStyle(headStyle);
			
			gl = 4;
		}

		HSSFCell dateCell = headrow2.createCell(gl + 1);
		HSSFCell dateCell2 = headrow2.createCell(gl + 2);
		HSSFCell dateCell3 = headrow2.createCell(gl + 3);
		HSSFCell dateCell4 = headrow2.createCell(gl + 4);
		HSSFCell dateCell5 = headrow2.createCell(gl + 5);
		HSSFCell dateCell6 = headrow2.createCell(gl + 6);
		HSSFCell dateCell7 = headrow2.createCell(gl + 7);
		HSSFCell dateCell8 = headrow2.createCell(gl + 8);
		HSSFCell dateCell9 = headrow2.createCell(gl + 9);
		HSSFCell dateCell10 = headrow2.createCell(gl + 10);
		HSSFCell dateCell11 = headrow2.createCell(gl + 11);
		HSSFCell dateCell12 = headrow2.createCell(gl + 12);
		
		dateCell.setCellValue("1");
		dateCell2.setCellValue("2");
		dateCell3.setCellValue("3");
		dateCell4.setCellValue("4");
		dateCell5.setCellValue("5");
		dateCell6.setCellValue("6");
		dateCell7.setCellValue("7");
		dateCell8.setCellValue("8");
		dateCell9.setCellValue("9");
		dateCell10.setCellValue("10");
		dateCell11.setCellValue("11");
		dateCell12.setCellValue("12");
		
		dateCell.setCellStyle(headStyle);
		dateCell2.setCellStyle(headStyle);
		dateCell3.setCellStyle(headStyle);
		dateCell4.setCellStyle(headStyle);
		dateCell5.setCellStyle(headStyle);
		dateCell6.setCellStyle(headStyle);
		dateCell7.setCellStyle(headStyle);
		dateCell8.setCellStyle(headStyle);
		dateCell9.setCellStyle(headStyle);
		dateCell10.setCellStyle(headStyle);
		dateCell11.setCellStyle(headStyle);
		dateCell12.setCellStyle(headStyle);
		
		rowCnt++;
		HSSFRow[] dataRow1 = new HSSFRow[dataList.size()];

		for (int i = 0; i < dataRow1.length; ++i) {
			RecordDto record = (RecordDto) dataList.get(i);
			dataRow1[i] = sheet1.createRow(rowCnt);
			if (!record.getString("ACT_ID").equals("sum")) {
				if (model.get("sheetNames1").toString().equals("가스기술공사 제보실적")) {
					HSSFCell dataCell = dataRow1[i].createCell(0);
					HSSFCell dataCell2 = dataRow1[i].createCell(1);
					HSSFCell dataCell3 = dataRow1[i].createCell(2);
					HSSFCell dataCell4 = dataRow1[i].createCell(3);
					
					dataCell.setCellValue((double) (i + 1));
					dataCell2.setCellValue(record.getString("ACT_ID"));
					dataCell3.setCellValue(record.getString("INFORMER_NAME"));
					dataCell4.setCellValue(record.getString("SUM_CNT"));
					
					dataCell.setCellStyle(dataStyle);
					dataCell2.setCellStyle(dataStyle);
					dataCell3.setCellStyle(dataStyle);
					dataCell4.setCellStyle(dataStyle);
				} else {
					HSSFCell dataCell = dataRow1[i].createCell(0);
					HSSFCell dataCell2 = dataRow1[i].createCell(1);
					HSSFCell dataCell3 = dataRow1[i].createCell(2);
					HSSFCell dataCell4 = dataRow1[i].createCell(3);
					HSSFCell dataCell5 = dataRow1[i].createCell(4);
					
					dataCell.setCellValue((double) (i + 1));
					dataCell2.setCellValue(record.getString("ACT_ID"));
					dataCell3.setCellValue(record.getString("INFORMER_NAME"));
					dataCell4.setCellValue(record.getString("PHONE_CELL"));
					dataCell5.setCellValue(Integer.parseInt(record.getString("SUM_CNT")));
					
					dataCell.setCellStyle(dataStyle);
					dataCell2.setCellStyle(dataStyle);
					dataCell3.setCellStyle(dataStyle);
					dataCell4.setCellStyle(dataStyle);
					dataCell5.setCellStyle(dataStyle);
				}
			} else {
				HSSFCell dataCell = dataRow1[i].createCell(0);
				
				dataCell.setCellValue("합      계");
				dataCell.setCellStyle(headStyle);
				
				if (model.get("sheetNames1").toString().equals("가스기술공사 제보실적")) {
					dataRow1[i].createCell(1).setCellStyle(headStyle);
					dataRow1[i].createCell(2).setCellStyle(headStyle);
				} else {
					dataRow1[i].createCell(1).setCellStyle(headStyle);
					dataRow1[i].createCell(2).setCellStyle(headStyle);
					dataRow1[i].createCell(3).setCellStyle(headStyle);
				}
				
				sheet1.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, gl - 1));
				
				HSSFCell sumCell = dataRow1[i].createCell(gl);
				sumCell.setCellValue(record.getString("SUM_CNT"));
				sumCell.setCellStyle(dataStyle);
			}

			HSSFCell sumCell = dataRow1[i].createCell(gl + 1);
			HSSFCell sumCell2 = dataRow1[i].createCell(gl + 2);
			HSSFCell sumCell3 = dataRow1[i].createCell(gl + 3);
			HSSFCell sumCell4 = dataRow1[i].createCell(gl + 4);
			HSSFCell sumCell5 = dataRow1[i].createCell(gl + 5);
			HSSFCell sumCell6 = dataRow1[i].createCell(gl + 6);
			HSSFCell sumCell7 = dataRow1[i].createCell(gl + 7);
			HSSFCell sumCell8 = dataRow1[i].createCell(gl + 8);
			HSSFCell sumCell9 = dataRow1[i].createCell(gl + 9);
			HSSFCell sumCell10 = dataRow1[i].createCell(gl + 10);
			HSSFCell sumCell11 = dataRow1[i].createCell(gl + 11);
			HSSFCell sumCell12 = dataRow1[i].createCell(gl + 12);
			
			/*sumCell.setCellValue(Integer.parseInt(record.getString("D1")));
			sumCell2.setCellValue(Integer.parseInt(record.getString("D2")));
			sumCell3.setCellValue(Integer.parseInt(record.getString("D3")));
			sumCell4.setCellValue(Integer.parseInt(record.getString("D4")));
			sumCell5.setCellValue(Integer.parseInt(record.getString("D5")));
			sumCell6.setCellValue(Integer.parseInt(record.getString("D6")));
			sumCell7.setCellValue(Integer.parseInt(record.getString("D7")));
			sumCell8.setCellValue(Integer.parseInt(record.getString("D8")));
			sumCell9.setCellValue(Integer.parseInt(record.getString("D9")));
			sumCell10.setCellValue(Integer.parseInt(record.getString("D10")));
			sumCell11.setCellValue(Integer.parseInt(record.getString("D11")));
			sumCell12.setCellValue(Integer.parseInt(record.getString("D12")));*/
			
			sumCell.setCellValue(record.getString("D1") != null && !record.getString("D1").isEmpty() ? Integer.parseInt(record.getString("D1")) : 0);
			sumCell2.setCellValue(record.getString("D2") != null && !record.getString("D2").isEmpty() ? Integer.parseInt(record.getString("D2")) : 0);
			sumCell3.setCellValue(record.getString("D3") != null && !record.getString("D3").isEmpty() ? Integer.parseInt(record.getString("D3")) : 0);
			sumCell4.setCellValue(record.getString("D4") != null && !record.getString("D4").isEmpty() ? Integer.parseInt(record.getString("D4")) : 0);
			sumCell5.setCellValue(record.getString("D5") != null && !record.getString("D5").isEmpty() ? Integer.parseInt(record.getString("D5")) : 0);
			sumCell6.setCellValue(record.getString("D6") != null && !record.getString("D6").isEmpty() ? Integer.parseInt(record.getString("D6")) : 0);
			sumCell7.setCellValue(record.getString("D7") != null && !record.getString("D7").isEmpty() ? Integer.parseInt(record.getString("D7")) : 0);
			sumCell8.setCellValue(record.getString("D8") != null && !record.getString("D8").isEmpty() ? Integer.parseInt(record.getString("D8")) : 0);
			sumCell9.setCellValue(record.getString("D9") != null && !record.getString("D9").isEmpty() ? Integer.parseInt(record.getString("D9")) : 0);
			sumCell10.setCellValue(record.getString("D10") != null && !record.getString("D10").isEmpty() ? Integer.parseInt(record.getString("D10")) : 0);
			sumCell11.setCellValue(record.getString("D11") != null && !record.getString("D11").isEmpty() ? Integer.parseInt(record.getString("D11")) : 0);
			sumCell12.setCellValue(record.getString("D12") != null && !record.getString("D12").isEmpty() ? Integer.parseInt(record.getString("D12")) : 0);

			
			sumCell.setCellStyle(dataStyle);
			sumCell2.setCellStyle(dataStyle);
			sumCell3.setCellStyle(dataStyle);
			sumCell4.setCellStyle(dataStyle);
			sumCell5.setCellStyle(dataStyle);
			sumCell6.setCellStyle(dataStyle);
			sumCell7.setCellStyle(dataStyle);
			sumCell8.setCellStyle(dataStyle);
			sumCell9.setCellStyle(dataStyle);
			sumCell10.setCellStyle(dataStyle);
			sumCell11.setCellStyle(dataStyle);
			sumCell12.setCellStyle(dataStyle);
			
			rowCnt++;
		}
		
		sheet1.setColumnWidth(0, 4000);
		sheet1.setColumnWidth(1, 6000);
		sheet1.setColumnWidth(2, 6000);
		sheet1.setColumnWidth(3, 6000);
		sheet1.setColumnWidth(4, 4000);
		
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 17));

    }

    
    
    // 통신원 목록 ( 통신원 관리 > 통신원 목록 엑셀 다운로드 )
    protected void informerDown(Map model, HSSFWorkbook wb) {
    	
    	CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
		int rowCnt = 0;
		List dataList = (List) model.get("Data");
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		HSSFCell titleCell = titlerow.createCell(rowCnt);
		titleCell.setCellValue(model.get("titleName").toString());
		titleCell.setCellStyle(titleStyle);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

		rowCnt++;
		
		HSSFRow headrow0 = sheet1.createRow(rowCnt);
		HSSFCell countCell = headrow0.createCell(0);
		countCell.setCellValue("합계 : " + dataList.size() + "건");
		countCell.setCellStyle(mainStyle);
		
		sheet1.setColumnWidth(0, 6000);
		
		rowCnt++;
		HSSFRow headrow1 = sheet1.createRow(rowCnt);
		
		List<String> headerList = new ArrayList<>();
		headerList.add("ID");
		headerList.add("방송국");
		headerList.add("유형");
		headerList.add("소속기관");
		headerList.add("이름");
		headerList.add("전화");
		headerList.add("활동여부");
		headerList.add("등록일");
		headerList.add("우편 번호");
		headerList.add("주소");

		List<String> selectData = (List<String>) model.get("selectData");
		
		
		if (selectData != null) {
			for (String col : selectData) {
				if ("TRS_NO".equals(col)) {
					headerList.add("TRS");
		        } else if ("MEMO1".equals(col)) {
		        	headerList.add("메모");
		        } else if ("HONOR".equals(col)) {
		        	headerList.add("명예 통신원");
		        } else if ("BIRTHDAY".equals(col)) {
		        	headerList.add("생일");
		        } else if ("IDENTIFI_DATE".equals(col)) {
		        	headerList.add("신분증 유효기간");
		        } else if ("FLAG_SERVICE".equals(col)) {
		        	headerList.add("자원봉사");
		        } else if ("MEMO".equals(col)) {
		        	headerList.add("전달사항");
		        } else if ("ADDRESS_OFFICE".equals(col)) {
		        	headerList.add("주소(회사)");
		        } else if ("PHONE_HOME".equals(col)) {
		        	headerList.add("집 전화번호");
		        } else if ("CAR_NUM".equals(col)) {
		        	headerList.add("차량번호");
		        } else if ("CAR_TYPE".equals(col)) {
		        	headerList.add("차량종류");
		        } else if ("FLAG_BEST".equals(col)) {
		        	headerList.add("최고통신원");
		        } else if ("UPD_DATE".equals(col)) {
		        	headerList.add("최종수정일");
		        } else if ("LAST_SCHOOL".equals(col)) {
		        	headerList.add("최종학력");
		        } else if ("MEMO2".equals(col)) {
		        	headerList.add("추가메모");
		        } else if ("FLAG_BROAD".equals(col)) {
		        	headerList.add("방송통신원");
		        } else if ("INFORMER_JOB".equals(col)) {
		        	headerList.add("통신원 직업");
		        } else if ("PHONE_OFFICE".equals(col)) {
		        	headerList.add("회사 전화번호");
		        } 
	
			}
		}

		
		/*List<Function<InfrmVO, String>> extractors = Arrays.asList(
			    InfrmVO::getActId,
			    InfrmVO::getAreaName,
			    InfrmVO::getInformerTypeName,
			    InfrmVO::getOrgName,
			    InfrmVO::getInformerName,
			    InfrmVO::getPhoneCell,
			    r -> "Y".equals(r.getFlagAct()) ? "위촉" : "해촉",
			    InfrmVO::getRegDate,
			    InfrmVO::getAddressHome,
			    //신규추가 항목
			    InfrmVO::getTrsNo,
			    InfrmVO::getMemo1,
			    InfrmVO::getHonor,
			    InfrmVO::getBirthday,
			    InfrmVO::getIdentifiDate,
			    InfrmVO::getFlagService,
			    InfrmVO::getMemo,
			    InfrmVO::getAddressOffice,
			    InfrmVO::getPhoneHome,
			    InfrmVO::getCarNum,
			    InfrmVO::getCarType,
			    InfrmVO::getFlagBest,
			    InfrmVO::getUpdDate,
			    InfrmVO::getLastSchool,
			    InfrmVO::getMemo2,
			    InfrmVO::getFlagBroad,
			    InfrmVO::getInformerJob,
			    InfrmVO::getPhoneOffice
			);*/
		
		List<Function<InfrmVO, String>> extractors = new ArrayList<>();
		extractors.add(InfrmVO::getActId);
		extractors.add(InfrmVO::getAreaName);
		extractors.add(InfrmVO::getInformerTypeName);
		extractors.add(InfrmVO::getOrgName);
		extractors.add(InfrmVO::getInformerName);
		extractors.add(InfrmVO::getPhoneCell);
		//extractors.add(r -> "Y".equals(r.getFlagAct()) ? "위촉" : "해촉");
		extractors.add(InfrmVO::getFlagAct);
		extractors.add(InfrmVO::getRegDate);
		extractors.add(InfrmVO::getZipcode);
		extractors.add(InfrmVO::getAddressHome);
		
		
		Map<String, Function<InfrmVO, String>> newExtractorMap = new HashMap<>();

		newExtractorMap.put("TRS_NO", InfrmVO::getTrsNo);
		newExtractorMap.put("MEMO1", InfrmVO::getMemo1);
		newExtractorMap.put("HONOR", InfrmVO::getHonor);
		newExtractorMap.put("BIRTHDAY", InfrmVO::getBirthday);
		newExtractorMap.put("IDENTIFI_DATE", InfrmVO::getIdentifiDate);
		newExtractorMap.put("FLAG_SERVICE", InfrmVO::getFlagService);
		newExtractorMap.put("MEMO", InfrmVO::getMemo);
		newExtractorMap.put("ADDRESS_OFFICE", InfrmVO::getAddressOffice);
		newExtractorMap.put("PHONE_HOME", InfrmVO::getPhoneHome);
		newExtractorMap.put("CAR_NUM", InfrmVO::getCarNum);
		newExtractorMap.put("CAR_TYPE", InfrmVO::getCarType);
		newExtractorMap.put("FLAG_BEST", InfrmVO::getFlagBest);
		newExtractorMap.put("UPD_DATE", InfrmVO::getUpdDate);
		newExtractorMap.put("LAST_SCHOOL", InfrmVO::getLastSchool);
		newExtractorMap.put("MEMO2", InfrmVO::getMemo2);
		newExtractorMap.put("FLAG_BROAD", InfrmVO::getFlagBroad);
		newExtractorMap.put("INFORMER_JOB", InfrmVO::getInformerJob);
		newExtractorMap.put("PHONE_OFFICE", InfrmVO::getPhoneOffice);
		
		if (selectData != null) {
		    for (String col : selectData) {
		        Function<InfrmVO, String> extractor = newExtractorMap.get(col);
		        if (extractor != null) {
		            extractors.add(extractor);
		        }
		    }
		}
		
		for (int i = 0; i < headerList.size(); i++) {
		    HSSFCell cell = headrow1.createCell(i);
		    cell.setCellValue(headerList.get(i));
		    cell.setCellStyle(headStyle);
		    sheet1.setColumnWidth(i, 6000);
		}
		
		rowCnt++;
		HSSFRow[] dataRow = new HSSFRow[dataList.size()];
		
		for (int i = 0; i < dataList.size(); ++i) {
			
			dataRow[i] = sheet1.createRow(rowCnt + i);
			InfrmVO record = (InfrmVO) dataList.get(i);

			// 사용부
			for (int col = 0; col < extractors.size(); col++) {
			    HSSFCell cell = dataRow[i].createCell(col);
			    String val = extractors.get(col).apply(record);
			    cell.setCellValue(val == null ? "" : val);
			    cell.setCellStyle(dataStyle);
			}
		}
		
		/*HSSFRow[] dataRow = new HSSFRow[dataList.size()];

		for (int i = 0; i < dataList.size(); ++i) {
			dataRow[i] = sheet1.createRow(rowCnt + i);
			InfrmVO record = (InfrmVO) dataList.get(i);
			
			HSSFCell dataCell0 = dataRow[i].createCell(0);
			dataCell0.setCellValue(record.getActId());
			dataCell0.setCellStyle(dataStyle);

			HSSFCell dataCell1 = dataRow[i].createCell(1);
			dataCell1.setCellValue(record.getAreaName());
			dataCell1.setCellStyle(dataStyle);

			HSSFCell dataCell2 = dataRow[i].createCell(2);
			dataCell2.setCellValue(record.getInformerTypeName());
			dataCell2.setCellStyle(dataStyle);

			HSSFCell dataCell3 = dataRow[i].createCell(3);
			dataCell3.setCellValue(record.getOrgName());
			dataCell3.setCellStyle(dataStyle);

			HSSFCell dataCell4 = dataRow[i].createCell(4);
			dataCell4.setCellValue(record.getInformerName());
			dataCell4.setCellStyle(dataStyle);

			HSSFCell dataCell5 = dataRow[i].createCell(5);
			dataCell5.setCellValue(record.getPhoneCell());
			dataCell5.setCellStyle(dataStyle);

			if (record.getFlagAct().equals("Y")) {
				HSSFCell dataCell = dataRow[i].createCell(6);
				dataCell.setCellValue("위촉");
				dataCell.setCellStyle(dataStyle);
			} else {
				HSSFCell dataCell = dataRow[i].createCell(6);
				dataCell.setCellValue("해촉");
				dataCell.setCellStyle(dataStyle);
			}

			HSSFCell dataCell = dataRow[i].createCell(7);
			dataCell.setCellValue(record.getRegDate());
			dataCell.setCellStyle(dataStyle);
			
			HSSFCell dataCell8 = dataRow[i].createCell(8);
			dataCell8.setCellValue(record.getAddressHome());
			dataCell8.setCellStyle(dataStyle);
		}*/
		
		sheet1.setColumnWidth(2, 4000);
		sheet1.setColumnWidth(3, 6000);
		sheet1.setColumnWidth(4, 6000);
		sheet1.setColumnWidth(5, 6000);
		sheet1.setColumnWidth(5, 6000);
		sheet1.setColumnWidth(6, 4000);
		sheet1.setColumnWidth(7, 6000);
		sheet1.setColumnWidth(8, 6000);
		sheet1.setColumnWidth(9, 25000);

	}
    

    // 24-11-20 :제보자별 제보현황
    protected void informerReport(Map model, HSSFWorkbook wb) {
    	int rowCnt = 0;
		List dataList = (List) model.get("dataList");
		
		// 뽑으려는 통계 제목
		String title = "제보자별 제보현황";
				
		// 통계제목 밑 총 인원 텍스트 구하기
		Object allInforValget = model.get("allInformer");
		String allInformerVal = (allInforValget != null) ? allInforValget.toString() : "0";
		String allInformer = "총 인원 : " + allInformerVal +"명";
		
		
		// 통계 제목 밑 일자 변환
		String getStartDate = (String) model.get("start_date");
		String startY = getStartDate.substring(0,4) + "년 ";
		String startM = getStartDate.substring(4,6) + "월 ";
		String startD = getStartDate.substring(6,8) + "일 ~ ";
				
		String startDate = startY + startM + startD;
				
		String getEndDate = (String) model.get("end_date");
		String endY = getEndDate.substring(0,4) + "년 ";
		String endM = getEndDate.substring(4,6) + "월 ";
		String endD = getEndDate.substring(6,8) + "일";
				
		String endDate = endY + endM + endD;
				
		HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
		HSSFRow titlerow = sheet1.createRow(rowCnt);
		
		CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용

		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    HSSFCell titleCell = titlerow.createCell(0);
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 26)); // 타이틀 셀 병합
		titleCell.setCellValue("제보자별 제보현황");// 제목 넣음
		titleCell.setCellStyle(titleStyle);
		
		rowCnt++; //1
		// 시작일 ~ 종료일
		HSSFRow daterow = sheet1.createRow(rowCnt);
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 26));
		HSSFCell dateCell = daterow.createCell(0);
		dateCell.setCellValue(startDate + endDate);
		dateCell.setCellStyle(mainStyle);
		
		rowCnt++; //2
		// 공백 행 하나 생성
		HSSFRow emptyRow1 = sheet1.createRow(rowCnt);
		
		rowCnt++; //3
		// 총 인원수 및 총 건수 행 생성
		HSSFRow subTitle1 = sheet1.createRow(rowCnt);
		sheet1.addMergedRegion(new CellRangeAddress(2, 2, 0, 2)); // 총인원 수 셀 병합
		sheet1.addMergedRegion(new CellRangeAddress(2, 2, 4, 6)); // 총 건수 셀 병합
		
		HSSFCell subTitleCell = subTitle1.createCell(0);
		subTitleCell.setCellValue(allInformer);
		subTitleCell.setCellStyle(mainStyle);
		/*subTitle1.createCell(2).setCellValue(allReport);*/
		
		rowCnt++; // 4
		// 통계 열 만들기
		HSSFRow rowTitle1 = sheet1.createRow(rowCnt);
		rowCnt++; // 5
		HSSFRow rowTitle2 = sheet1.createRow(rowCnt);
		
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 0, 0)); // ID 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 1, 1)); // 통신원 이름 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 2, 2)); // 통신원 유형 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 3, 3)); // 통신원 소속 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 4, 4)); // 통신원 소속 상세 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 5, 5)); // 통신원 연락처 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 6, 6)); // 제보건수 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 7, 7)); // 방송건수 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 5, 8, 8)); // 주요제보 행 병합	
		sheet1.addMergedRegion(new CellRangeAddress(4, 4, 9, 17)); // 제보건수 상세 행 병합
		sheet1.addMergedRegion(new CellRangeAddress(4, 4, 18, 26)); // 방송건수 상세 행 병합
		
		HSSFCell headCell0 = rowTitle1.createCell(0);
		headCell0.setCellValue("통신원 ID");
		headCell0.setCellStyle(headStyle);
		rowTitle2.createCell(0).setCellStyle(headStyle);
		
		HSSFCell headCell1 = rowTitle1.createCell(1);
		headCell1.setCellValue("통신원 이름");
		headCell1.setCellStyle(headStyle);
		rowTitle2.createCell(1).setCellStyle(headStyle);

		HSSFCell headCell2 = rowTitle1.createCell(2);
		headCell2.setCellValue("통신원 유형");
		headCell2.setCellStyle(headStyle);
		rowTitle2.createCell(2).setCellStyle(headStyle);

		HSSFCell headCell3 = rowTitle1.createCell(3);
		headCell3.setCellValue("통신원 소속");
		headCell3.setCellStyle(headStyle);
		rowTitle2.createCell(3).setCellStyle(headStyle);

		HSSFCell headCell4 = rowTitle1.createCell(4);
		headCell4.setCellValue("소속 상세");
		headCell4.setCellStyle(headStyle);
		rowTitle2.createCell(4).setCellStyle(headStyle);

		HSSFCell headCell5 = rowTitle1.createCell(5);
		headCell5.setCellValue("연락처");
		headCell5.setCellStyle(headStyle);
		rowTitle2.createCell(5).setCellStyle(headStyle);

		HSSFCell headCell6 = rowTitle1.createCell(6);
		headCell6.setCellValue("제보건수");
		headCell6.setCellStyle(headStyle);
		rowTitle2.createCell(6).setCellStyle(headStyle);

		HSSFCell headCell7 = rowTitle1.createCell(7);
		headCell7.setCellValue("방송건수");
		headCell7.setCellStyle(headStyle);
		rowTitle2.createCell(7).setCellStyle(headStyle);

		HSSFCell headCell8 = rowTitle1.createCell(8);
		headCell8.setCellValue("긴급제보");
		headCell8.setCellStyle(headStyle);
		rowTitle2.createCell(8).setCellStyle(headStyle);

		HSSFCell headCell9 = rowTitle1.createCell(9);
		headCell9.setCellValue("제보건수");
		headCell9.setCellStyle(headStyle);
		rowTitle1.createCell(10).setCellStyle(headStyle);
		rowTitle1.createCell(11).setCellStyle(headStyle);
		rowTitle1.createCell(12).setCellStyle(headStyle);
		rowTitle1.createCell(13).setCellStyle(headStyle);
		rowTitle1.createCell(14).setCellStyle(headStyle);
		rowTitle1.createCell(15).setCellStyle(headStyle);
		rowTitle1.createCell(16).setCellStyle(headStyle);
		rowTitle1.createCell(17).setCellStyle(headStyle);

		HSSFCell headCell18 = rowTitle1.createCell(18);
		headCell18.setCellValue("방송건수");
		headCell18.setCellStyle(headStyle);
		rowTitle1.createCell(19).setCellStyle(headStyle);
		rowTitle1.createCell(20).setCellStyle(headStyle);
		rowTitle1.createCell(21).setCellStyle(headStyle);
		rowTitle1.createCell(22).setCellStyle(headStyle);
		rowTitle1.createCell(23).setCellStyle(headStyle);
		rowTitle1.createCell(24).setCellStyle(headStyle);
		rowTitle1.createCell(25).setCellStyle(headStyle);
		rowTitle1.createCell(26).setCellStyle(headStyle);
		
		
		
		sheet1.setColumnWidth(0, 4000);
		sheet1.setColumnWidth(1, 5000);
		sheet1.setColumnWidth(2, 4000);
		sheet1.setColumnWidth(4, 5000);
		sheet1.setColumnWidth(5, 6000);
		sheet1.setColumnWidth(6, 5000);
		sheet1.setColumnWidth(7, 5000);
		sheet1.setColumnWidth(8, 5000);
		
		
		// rowTitle2 설정
		HSSFCell headCell9_2 = rowTitle2.createCell(9);
		headCell9_2.setCellValue("원활");
		headCell9_2.setCellStyle(headStyle);

		HSSFCell headCell10 = rowTitle2.createCell(10);
		headCell10.setCellValue("정체");
		headCell10.setCellStyle(headStyle);

		HSSFCell headCell11 = rowTitle2.createCell(11);
		headCell11.setCellValue("공사");
		headCell11.setCellStyle(headStyle);

		HSSFCell headCell12 = rowTitle2.createCell(12);
		headCell12.setCellValue("행사");
		headCell12.setCellStyle(headStyle);

		HSSFCell headCell13 = rowTitle2.createCell(13);
		headCell13.setCellValue("사고");
		headCell13.setCellStyle(headStyle);

		HSSFCell headCell14 = rowTitle2.createCell(14);
		headCell14.setCellValue("기상");
		headCell14.setCellStyle(headStyle);

		HSSFCell headCell15 = rowTitle2.createCell(15);
		headCell15.setCellValue("기타");
		headCell15.setCellStyle(headStyle);

		HSSFCell headCell16 = rowTitle2.createCell(16);
		headCell16.setCellValue("안내");
		headCell16.setCellStyle(headStyle);

		HSSFCell headCell17 = rowTitle2.createCell(17);
		headCell17.setCellValue("기관통보");
		headCell17.setCellStyle(headStyle);

		// 방송건수 상세 열
		HSSFCell headCell18_2 = rowTitle2.createCell(18);
		headCell18_2.setCellValue("원활");
		headCell18_2.setCellStyle(headStyle);

		HSSFCell headCell19 = rowTitle2.createCell(19);
		headCell19.setCellValue("정체");
		headCell19.setCellStyle(headStyle);

		HSSFCell headCell20 = rowTitle2.createCell(20);
		headCell20.setCellValue("공사");
		headCell20.setCellStyle(headStyle);

		HSSFCell headCell21 = rowTitle2.createCell(21);
		headCell21.setCellValue("행사");
		headCell21.setCellStyle(headStyle);

		HSSFCell headCell22 = rowTitle2.createCell(22);
		headCell22.setCellValue("사고");
		headCell22.setCellStyle(headStyle);

		HSSFCell headCell23 = rowTitle2.createCell(23);
		headCell23.setCellValue("기상");
		headCell23.setCellStyle(headStyle);

		HSSFCell headCell24 = rowTitle2.createCell(24);
		headCell24.setCellValue("기타");
		headCell24.setCellStyle(headStyle);

		HSSFCell headCell25 = rowTitle2.createCell(25);
		headCell25.setCellValue("안내");
		headCell25.setCellStyle(headStyle);

		HSSFCell headCell26 = rowTitle2.createCell(26);
		headCell26.setCellValue("기관통보");
		headCell26.setCellStyle(headStyle);

		DataFormat format = wb.createDataFormat();

		CellStyle intStyle = wb.createCellStyle();
 	    intStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
 	    intStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    intStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
 	    intStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setDataFormat(format.getFormat("#,##0"));
 	    
	    Font intStylefont = wb.createFont(); // 폰트 객체 생성
	    intStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	    intStyle.setFont(intStylefont); // 폰트 스타일을 셀 스타일에 적용
		
		
		rowCnt++;
		HSSFRow[] dataRow = new HSSFRow[dataList.size()];
		
		int allReport = 0;
		// 받아온 개수만큼 데이터 삽입
		for(int i = 0; i < dataList.size(); i++){
			dataRow[i] = sheet1.createRow(rowCnt + i);
			
			// dataList에서 각 row를 가져오고, 이를 Map으로 캐스팅
		    Map<String, Object> record = (Map<String, Object>) dataList.get(i);

		    int getReport = Integer.parseInt(record.get("제보건수").toString());		    
		    allReport = allReport + getReport;
		    
		    
		    
		    HSSFCell dataCell0 = dataRow[i].createCell(0);
		    dataCell0.setCellValue(record.get("통신원ID").toString());
		    dataCell0.setCellStyle(dataStyle);

		    HSSFCell dataCell1 = dataRow[i].createCell(1);
		    dataCell1.setCellValue(record.get("통신원이름").toString());
		    dataCell1.setCellStyle(dataStyle);

		    HSSFCell dataCell2 = dataRow[i].createCell(2);
		    dataCell2.setCellValue("통신원"); // 어차피 통신원(0)만 추출하기 때문에 필요 없음
		    dataCell2.setCellStyle(dataStyle);

		    HSSFCell dataCell3 = dataRow[i].createCell(3);
		    dataCell3.setCellValue(record.get("통신원소속").toString());
		    dataCell3.setCellStyle(dataStyle);

		    HSSFCell dataCell4 = dataRow[i].createCell(4);
		    dataCell4.setCellValue(record.get("소속상세").toString());
		    dataCell4.setCellStyle(dataStyle);

		    HSSFCell dataCell5 = dataRow[i].createCell(5);
		    dataCell5.setCellValue(record.get("연락처").toString());
		    dataCell5.setCellStyle(dataStyle);

		    HSSFCell dataCell6 = dataRow[i].createCell(6);
		    dataCell6.setCellValue(Integer.parseInt(record.get("제보건수").toString()));
		    dataCell6.setCellStyle(intStyle);

		    HSSFCell dataCell7 = dataRow[i].createCell(7);
		    dataCell7.setCellValue(Integer.parseInt(record.get("방송건수").toString()));
		    dataCell7.setCellStyle(intStyle);

		    HSSFCell dataCell8 = dataRow[i].createCell(8);
		    dataCell8.setCellValue(Integer.parseInt(record.get("주요제보").toString()));
		    dataCell8.setCellStyle(intStyle);

		    HSSFCell dataCell9 = dataRow[i].createCell(9);
		    dataCell9.setCellValue(Integer.parseInt(record.get("제보건수_원활").toString()));
		    dataCell9.setCellStyle(intStyle);

		    HSSFCell dataCell10 = dataRow[i].createCell(10);
		    dataCell10.setCellValue(Integer.parseInt(record.get("제보건수_정체").toString()));
		    dataCell10.setCellStyle(intStyle);

		    HSSFCell dataCell11 = dataRow[i].createCell(11);
		    dataCell11.setCellValue(Integer.parseInt(record.get("제보건수_공사").toString()));
		    dataCell11.setCellStyle(dataStyle);

		    HSSFCell dataCell12 = dataRow[i].createCell(12);
		    dataCell12.setCellValue(Integer.parseInt(record.get("제보건수_행사").toString()));
		    dataCell12.setCellStyle(intStyle);

		    HSSFCell dataCell13 = dataRow[i].createCell(13);
		    dataCell13.setCellValue(Integer.parseInt(record.get("제보건수_사고").toString()));
		    dataCell13.setCellStyle(intStyle);

		    HSSFCell dataCell14 = dataRow[i].createCell(14);
		    dataCell14.setCellValue(Integer.parseInt(record.get("제보건수_기상").toString()));
		    dataCell14.setCellStyle(dataStyle);

		    HSSFCell dataCell15 = dataRow[i].createCell(15);
		    dataCell15.setCellValue(Integer.parseInt(record.get("제보건수_기타").toString()));
		    dataCell15.setCellStyle(intStyle);

		    HSSFCell dataCell16 = dataRow[i].createCell(16);
		    dataCell16.setCellValue(Integer.parseInt(record.get("제보건수_안내").toString()));
		    dataCell16.setCellStyle(intStyle);

		    HSSFCell dataCell17 = dataRow[i].createCell(17);
		    dataCell17.setCellValue(Integer.parseInt(record.get("제보건수_기관통보").toString()));
		    dataCell17.setCellStyle(intStyle);

		    HSSFCell dataCell18 = dataRow[i].createCell(18);
		    dataCell18.setCellValue(Integer.parseInt(record.get("방송건수_원활").toString()));
		    dataCell18.setCellStyle(intStyle);

		    HSSFCell dataCell19 = dataRow[i].createCell(19);
		    dataCell19.setCellValue(Integer.parseInt(record.get("방송건수_정체").toString()));
		    dataCell19.setCellStyle(intStyle);

		    HSSFCell dataCell20 = dataRow[i].createCell(20);
		    dataCell20.setCellValue(Integer.parseInt(record.get("방송건수_공사").toString()));
		    dataCell20.setCellStyle(intStyle);

		    HSSFCell dataCell21 = dataRow[i].createCell(21);
		    dataCell21.setCellValue(Integer.parseInt(record.get("방송건수_행사").toString()));
		    dataCell21.setCellStyle(intStyle);

		    HSSFCell dataCell22 = dataRow[i].createCell(22);
		    dataCell22.setCellValue(Integer.parseInt(record.get("방송건수_사고").toString()));
		    dataCell22.setCellStyle(intStyle);

		    HSSFCell dataCell23 = dataRow[i].createCell(23);
		    dataCell23.setCellValue(Integer.parseInt(record.get("방송건수_기상").toString()));
		    dataCell23.setCellStyle(intStyle);

		    HSSFCell dataCell24 = dataRow[i].createCell(24);
		    dataCell24.setCellValue(Integer.parseInt(record.get("방송건수_기타").toString()));
		    dataCell24.setCellStyle(intStyle);

		    HSSFCell dataCell25 = dataRow[i].createCell(25);
		    dataCell25.setCellValue(Integer.parseInt(record.get("방송건수_안내").toString()));
		    dataCell25.setCellStyle(intStyle);

		    HSSFCell dataCell26 = dataRow[i].createCell(26);
		    dataCell26.setCellValue(Integer.parseInt(record.get("방송건수_기관통보").toString()));
		    dataCell26.setCellStyle(intStyle);

			
		    
		}
		
		// 총 건수 넣기
		String allReportVal = "총 건수 : " + allReport +"건";
		HSSFCell subTitleCell2 = subTitle1.createCell(2);
		subTitleCell2.setCellValue(allReportVal);
		subTitleCell2.setCellStyle(mainStyle);
		
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

    }
    
    
    // 통신원 중/소 분류별 통계
    protected void orgOrgSub(Map model, HSSFWorkbook wb) {
        List dataList = (List) model.get("orgOrgSub");
        List eraList = (List) model.get("eraList");
        HSSFSheet sheet1 = wb.createSheet(model.get("sheetNames1").toString());
        
        HSSFRow titlerow = sheet1.createRow(0);
        
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 24); // 폰트 크기 설정
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    HSSFCell titleCell = titlerow.createCell(0);
 	    titleCell.setCellValue(model.get("titleName").toString());
 	    titleCell.setCellStyle(titleStyle);
       
        
 	    CellStyle headStyle = wb.createCellStyle();
    	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
    	headStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
    	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
    	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
    	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		// 추가 셀 스타일 제작
		CellStyle headStyle2 = wb.createCellStyle();
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
    	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		
    	DataFormat format = wb.createDataFormat();
    	
		CellStyle mainStyle = wb.createCellStyle();
		mainStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
		mainStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font mainStylefont = wb.createFont(); // 폰트 객체 생성
 	    mainStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    mainStyle.setFont(mainStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
        int rowCnt = 0 + 1;
        HSSFRow headrow0 = sheet1.createRow(rowCnt);
        
        HSSFCell cntCell = headrow0.createCell(0);
        HSSFCell cntvalCell = headrow0.createCell(1);
        
        cntCell.setCellValue("건수 : ");
        cntvalCell.setCellValue(dataList.size());
        
        cntCell.setCellStyle(mainStyle);
        cntvalCell.setCellStyle(mainStyle);
        
        
        int rowCnt2 = rowCnt + 1;
        HSSFRow headrow1 = sheet1.createRow(rowCnt2);
        
        HSSFCell headCell = headrow1.createCell(0);
        HSSFCell headCell2 = headrow1.createCell(1);
        HSSFCell headCell3 = headrow1.createCell(2);
        HSSFCell headCell4 = headrow1.createCell(3);
        HSSFCell headCell5 = headrow1.createCell(4);
        HSSFCell headCell6 = headrow1.createCell(5);
        HSSFCell headCell7 = headrow1.createCell(6);
        HSSFCell headCell8 = headrow1.createCell(7);
        HSSFCell headCell9 = headrow1.createCell(8);
        
        headCell.setCellValue("연번");
        headCell2.setCellValue("ID");
        headCell3.setCellValue("성명");
        headCell4.setCellValue("전화번호");
        headCell5.setCellValue("중분류");
        headCell6.setCellValue("소분류");
        headCell7.setCellValue("생일");
        headCell8.setCellValue("월계");
        
        headCell.setCellStyle(headStyle);
        headCell2.setCellStyle(headStyle);
        headCell3.setCellStyle(headStyle);
        headCell4.setCellStyle(headStyle);
        headCell5.setCellStyle(headStyle);
        headCell6.setCellStyle(headStyle);
        headCell7.setCellStyle(headStyle);
        headCell8.setCellStyle(headStyle);
        headCell9.setCellStyle(headStyle2);
        
        int nxtCnt =7;
        
        for (int i = 1; i <= eraList.size(); i++) {
        	 RecordDto record1 = (RecordDto) eraList.get(i-1);
        	 String key_date = record1.getString("KEY_DATE");
        	 key_date=key_date.replaceAll("date_", "");
        	 key_date=key_date.substring(0, 4)+"년 "+key_date.substring(4, 6)+"월";
        	 
        	 HSSFCell dataCell = headrow1.createCell(nxtCnt+i);
        	 dataCell.setCellValue(key_date);
        	 dataCell.setCellStyle(headStyle);
        	 
             sheet1.setColumnWidth(nxtCnt+i, 6000);
		}
        
        int mergedSize = (eraList.size() + 7);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, mergedSize));
        
        

        CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    
 	    Font dataStylefont = wb.createFont(); // 폰트 객체 생성
 	    dataStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
 	    dataStyle.setFont(dataStylefont); // 폰트 스타일을 셀 스타일에 적용

 	    
 	    CellStyle intStyle = wb.createCellStyle();
 	    intStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
 	    intStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    intStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
 	    intStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
 	    intStyle.setDataFormat(format.getFormat("#,##0"));
 	    
	    Font intStylefont = wb.createFont(); // 폰트 객체 생성
	    intStylefont.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	    intStyle.setFont(intStylefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
        int rowCnt3 = rowCnt2 + 1;
        HSSFRow[] dataRow = new HSSFRow[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataRow[i] = sheet1.createRow(rowCnt3 + i);
            RecordDto record = (RecordDto) dataList.get(i);
            
            HSSFCell dataCell = dataRow[i].createCell(0);
            HSSFCell dataCell2 = dataRow[i].createCell(1);
            HSSFCell dataCell3 = dataRow[i].createCell(2);
            HSSFCell dataCell4 = dataRow[i].createCell(3);
            HSSFCell dataCell5 = dataRow[i].createCell(4);
            HSSFCell dataCell6 = dataRow[i].createCell(5);
            HSSFCell dataCell7 = dataRow[i].createCell(6);
            HSSFCell dataCell8 = dataRow[i].createCell(7);
            
            dataCell.setCellValue(i + 1);
            dataCell2.setCellValue(record.getString("ACT_ID"));
            dataCell3.setCellValue(record.getString("INFORMER_NAME"));
            dataCell4.setCellValue(record.getString("PHONE_CELL"));
            dataCell5.setCellValue(record.getString("ORG_NAME"));
            dataCell6.setCellValue(record.getString("ORG_SNAME"));
            dataCell7.setCellValue(record.getString("BIRTHDAY"));
            dataCell8.setCellValue(Integer.parseInt(record.getString("SUM_CNT")));
            
            dataCell.setCellStyle(dataStyle);
            dataCell2.setCellStyle(dataStyle);
            dataCell3.setCellStyle(dataStyle);
            dataCell4.setCellStyle(dataStyle);
            dataCell5.setCellStyle(dataStyle);
            dataCell6.setCellStyle(dataStyle);
            dataCell7.setCellStyle(dataStyle);
            dataCell8.setCellStyle(intStyle);
               
//            for (int j = 1; j <= 31; j++) {
//            	dataRow[i].createCell(nxtCnt+j).setCellValue(record.getString("D"+j));
//            }
            for (int j = 1; j <= eraList.size(); j++) {
	           	 RecordDto record2 = (RecordDto) eraList.get(j-1);
	           	 String key_date = record2.getString("KEY_DATE");
	           	 
	           	 HSSFCell dateCell = dataRow[i].createCell(nxtCnt+j);
	           	 dateCell.setCellValue(Integer.parseInt(record.getString(key_date)));
	           	 dateCell.setCellStyle(intStyle);
	   		}
        }
        
        sheet1.setColumnWidth(1, 6000);
        sheet1.setColumnWidth(2, 6000);
        sheet1.setColumnWidth(3, 6000);
        sheet1.setColumnWidth(4, 6000);
        sheet1.setColumnWidth(5, 6000);
        sheet1.setColumnWidth(6, 6000);
        sheet1.setColumnWidth(7, 6000);
        
    }
    
    
    // 주소라벨 출력
    public void addDownload(Map model, HSSFWorkbook wb) {
    	String pickLabelT = (String) model.get("labelType"); 
    	int labelType = Integer.parseInt(pickLabelT); // 라벨 타입 가져오기 16 or 24
    	
    	// 엑셀에서 사용할 데이터 리스트 가져오기
    	List dataList = (List) model.get("dataList");
    	
    	int dataCnt = dataList.size(); // 받아온 데이터 전체 수
    	/*int writeCnt = 0; // 데이터 넣은 카운터
*/    	int dataCnt16; //sheet를 위한 변수 (16라벨)
    	int dataCnt24; //sheet를 위한 변수 (24라벨)
    	
    	int rowCnt = 0;
    	
    	// 16라벨일 때 ( 2 * 8 )
    	if(labelType == 16) {
    		
    		// 전체 sheet 수 구하기
    		if(dataCnt < 16) { // 받아온 데이터가 16개가 안될 때 
    			dataCnt16 = 1; 
    		} else { // 받아온 데이터가 16개 이상일 때
    			dataCnt16 = dataCnt/16 ;
    			int dataCnt16Chk = dataCnt%16; // 나머지 구하기 
    			
    			if(dataCnt16Chk > 0) { //나머지가 있다면
    				dataCnt16 = dataCnt16 + 1; // sheet 수 1개 더 추가
    			}
    		}
            
    		
            // 엑셀 생성하기
    		for(int i = 0; i < dataCnt16 ; i++) { // sheet 생성 for문
    			HSSFSheet sheet = wb.createSheet("label(" + (i+1) + ")"); // label(n) 형식으로 시트 생성  
    			
    			int cellRowCnt = (i * 8) * 2;
                for(int j = 0 ; j < 8; j++) {
                	for(int x = 0; x < 1; x++) {
                		HSSFRow headrow = sheet.createRow(rowCnt); // row 생성, 주소 입력
                		HSSFRow headrow1 = sheet.createRow(rowCnt + 1); // row 생성, 이름 입력
                		HSSFRow headrow2 = sheet.createRow(rowCnt + 2); // row 생성, 전화번호 및 우편번호 입력
                		
                		/*InfrmVO record = (InfrmVO) dataList.get(cellRowCnt);*/ // 라벨 row의 1번 라벨
                		if(cellRowCnt < dataCnt) {
                			Object pickRecord = dataList.get(cellRowCnt);
                    		InfrmVO record = (InfrmVO) pickRecord;
                    		
                    		// 폰트 및 스타일 설정
                    	    CellStyle cellStyle = wb.createCellStyle();
                    	    CellStyle cellStyle1 = wb.createCellStyle();
                    	    CellStyle cellStyle2 = wb.createCellStyle();
                    	    CellStyle cellStyle3 = wb.createCellStyle();
                    	    CellStyle cellStyle4 = wb.createCellStyle();
                    	    
                    	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
                    	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	  
                    	    cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    Font font = wb.createFont();
                    	    font.setFontHeightInPoints((short) 12); // 폰트 크기 12px로 설정
                    	    cellStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

                    	    
                    	    Font font1 = wb.createFont();
                    	    font1.setFontHeightInPoints((short) 14);
                    	    font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle1.setFont(font1); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    Font font2 = wb.createFont();
                    	    font2.setFontHeightInPoints((short) 10);
                    	    cellStyle2.setFont(font2); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    
                    	    Font font3 = wb.createFont();
                    	    font3.setFontHeightInPoints((short) 11);
                    	    font3.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle3.setFont(font3); 

                    	    Font font4 = wb.createFont();
                    	    font4.setFontHeightInPoints((short) 12);
                    	    font4.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle4.setFont(font4); // 폰트 스타일을 셀 스타일에 적용
                    	 
                    		
                    	 // 주소 셀 0 (headrow) 생성 및 스타일 적용
                    	    HSSFCell cell0 = headrow.createCell(0);
                    	    cell0.setCellValue("none".equals(record.getAddressHome()) ? "" : record.getAddressHome()); // 주소 입력
                    	    cell0.setCellStyle(cellStyle); // 스타일 적용
                    	    sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 1)); // 주소 입력 란 셀 병합

                    	    // 이름 셀 0 (headrow1) 생성 및 스타일 적용
                    	    HSSFCell cell1 = headrow1.createCell(0);
                    	    cell1.setCellValue("none".equals(record.getInformerName()) ? "" : record.getInformerName()); // 이름 입력
                    	    cell1.setCellStyle(cellStyle1); // 스타일 적용

                    	    // 통신원 셀 1 (headrow1) 생성
                    	    HSSFCell cell2 = headrow1.createCell(1);
                    	    cell2.setCellValue("통신원님 귀하");
                    	    cell2.setCellStyle(cellStyle2); // 스타일 적용

                    	    // 전화 번호 셀 0 (headrow2) 생성 및 스타일 적용
                    	    HSSFCell cell3 = headrow2.createCell(0);
                    	    cell3.setCellValue("none".equals(record.getPhoneCell()) ? "" : record.getPhoneCell()); // 전화 번호 입력
                    	    cell3.setCellStyle(cellStyle3); // 스타일 적용

                    	    // 우편번호 셀 1 (headrow2) 생성
                    	    HSSFCell cell4 = headrow2.createCell(1);
                    	    cell4.setCellValue("none".equals(record.getZipcode()) ? "" : record.getZipcode()); // 우편번호 입력
                    	    cell4.setCellStyle(cellStyle4); // 스타일 적용

                    	    // 시트 설정
                    	    sheet.setColumnWidth(0, 5700);
                    	    sheet.setColumnWidth(1, 5700);
                    	    headrow.setHeightInPoints(47.33f); // row height
                    	    
                    	    

                		} else {
                			break;
                		}
                		
                		sheet.setColumnWidth(2, 700);
                		
                		
                		if((cellRowCnt+1) < dataCnt){
                			Object pickRecord2 = dataList.get(cellRowCnt+1);
                    		InfrmVO record2 = (InfrmVO) pickRecord2;
                    		
                    		// 폰트 및 스타일 설정
                    	    CellStyle cellStyle = wb.createCellStyle();
                    	    CellStyle cellStyle1 = wb.createCellStyle();
                    	    CellStyle cellStyle2 = wb.createCellStyle();
                    	    CellStyle cellStyle3 = wb.createCellStyle();
                    	    CellStyle cellStyle4 = wb.createCellStyle();
                    	    
                    	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
                    	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	  
                    	    cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    Font font = wb.createFont();
                    	    font.setFontHeightInPoints((short) 12); // 폰트 크기 12px로 설정
                    	    cellStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

                    	    
                    	    Font font1 = wb.createFont();
                    	    font1.setFontHeightInPoints((short) 14);
                    	    font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle1.setFont(font1); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    Font font2 = wb.createFont();
                    	    font2.setFontHeightInPoints((short) 10);
                    	    cellStyle2.setFont(font2); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    
                    	    Font font3 = wb.createFont();
                    	    font3.setFontHeightInPoints((short) 11);
                    	    font3.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle3.setFont(font3); 

                    	    Font font4 = wb.createFont();
                    	    font4.setFontHeightInPoints((short) 12);
                    	    font4.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle4.setFont(font4); // 폰트 스타일을 셀 스타일에 적용
                    	 
                    		
                    	 // 주소 셀 0 (headrow) 생성 및 스타일 적용
                    	    HSSFCell cell0 = headrow.createCell(3);
                    	    cell0.setCellValue("none".equals(record2.getAddressHome()) ? "" : record2.getAddressHome()); // 주소 입력
                    	    cell0.setCellStyle(cellStyle); // 스타일 적용
                    	    sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 3, 4)); // 주소 입력 란 셀 병합

                    	    // 이름 셀 0 (headrow1) 생성 및 스타일 적용
                    	    HSSFCell cell1 = headrow1.createCell(3);
                    	    cell1.setCellValue("none".equals(record2.getInformerName()) ? "" : record2.getInformerName()); // 이름 입력
                    	    cell1.setCellStyle(cellStyle1); // 스타일 적용

                    	    // 통신원 셀 1 (headrow1) 생성
                    	    HSSFCell cell2 = headrow1.createCell(4);
                    	    cell2.setCellValue("통신원님 귀하");
                    	    cell2.setCellStyle(cellStyle2); // 스타일 적용

                    	    // 전화 번호 셀 0 (headrow2) 생성 및 스타일 적용
                    	    HSSFCell cell3 = headrow2.createCell(3);
                    	    cell3.setCellValue("none".equals(record2.getPhoneCell()) ? "" : record2.getPhoneCell()); // 전화 번호 입력
                    	    cell3.setCellStyle(cellStyle3); // 스타일 적용

                    	    // 우편번호 셀 1 (headrow2) 생성
                    	    HSSFCell cell4 = headrow2.createCell(4);
                    	    cell4.setCellValue("none".equals(record2.getZipcode()) ? "" : record2.getZipcode()); // 우편번호 입력
                    	    cell4.setCellStyle(cellStyle4); // 스타일 적용
                    		
                    		// 시트 설정
                    		sheet.setColumnWidth(3, 5700);
                    		sheet.setColumnWidth(4, 5700);
                    		headrow.setHeightInPoints(47.33f); // row height
                		} else {
                			break;
                		}
              		
                	}
            		
                	rowCnt = rowCnt + 3;
                	
                	cellRowCnt = cellRowCnt + 2;
                	
                }
                
                // 값 초기화
                rowCnt = 0;
/*                dataCnt = dataCnt-16;*/
    		}
            

    	} else { // 24라벨일 때 ( 3 * 8 )
    		
    		// 전체 sheet 수 구하기
    		if(dataCnt < 24) { // 받아온 데이터가 16개가 안될 때 
    			dataCnt24 = 1; 
    		} else { // 받아온 데이터가 16개 이상일 때
    			dataCnt24 = dataCnt/24 ;
    			int dataCnt24Chk = dataCnt % 24; // 나머지 구하기 
    			
    			if(dataCnt24Chk > 0) { //나머지가 있다면
    				dataCnt24 = dataCnt24 + 1; // sheet 수 1개 더 추가
    			}
    		}
    		
    		
    		// 엑셀 생성하기
    		for(int i = 0; i < dataCnt24 ; i++) { // sheet 생성 for문
    			HSSFSheet sheet = wb.createSheet("label(" + (i+1) + ")"); // label(n) 형식으로 시트 생성  
    			
    			int cellRowCnt = (i * 12) * 2;
                for(int j = 0 ; j < 8; j++) {
                	for(int x = 0; x < 1; x++) {
                		HSSFRow headrow = sheet.createRow(rowCnt); // row 생성, 주소 입력
                		HSSFRow headrow1 = sheet.createRow(rowCnt + 1); // row 생성, 이름 입력
                		HSSFRow headrow2 = sheet.createRow(rowCnt + 2); // row 생성, 전화번호 및 우편번호 입력
                		
                		/*InfrmVO record = (InfrmVO) dataList.get(cellRowCnt);*/ // 라벨 row의 1번 라벨
                		if(cellRowCnt < dataCnt) {
                			Object pickRecord = dataList.get(cellRowCnt);
                    		InfrmVO record = (InfrmVO) pickRecord;

                    		// 폰트 및 스타일 설정
                    	    CellStyle cellStyle = wb.createCellStyle();
                    	    CellStyle cellStyle1 = wb.createCellStyle();
                    	    CellStyle cellStyle2 = wb.createCellStyle();
                    	    CellStyle cellStyle3 = wb.createCellStyle();
                    	    CellStyle cellStyle4 = wb.createCellStyle();
                    	    
                    	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
                    	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	  
                    	    cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    Font font = wb.createFont();
                    	    font.setFontHeightInPoints((short) 12); // 폰트 크기 12px로 설정
                    	    cellStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

                    	    
                    	    Font font1 = wb.createFont();
                    	    font1.setFontHeightInPoints((short) 14);
                    	    font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle1.setFont(font1); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    Font font2 = wb.createFont();
                    	    font2.setFontHeightInPoints((short) 10);
                    	    cellStyle2.setFont(font2); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    
                    	    Font font3 = wb.createFont();
                    	    font3.setFontHeightInPoints((short) 11);
                    	    font3.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle3.setFont(font3); 

                    	    Font font4 = wb.createFont();
                    	    font4.setFontHeightInPoints((short) 12);
                    	    font4.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle4.setFont(font4); // 폰트 스타일을 셀 스타일에 적용
                    	 
                    		
                    	 // 주소 셀 0 (headrow) 생성 및 스타일 적용
                    	    HSSFCell cell0 = headrow.createCell(0);
                    	    cell0.setCellValue("none".equals(record.getAddressHome()) ? "" : record.getAddressHome()); // 주소 입력
                    	    cell0.setCellStyle(cellStyle); // 스타일 적용
                    	    sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 1)); // 주소 입력 란 셀 병합

                    	    // 이름 셀 0 (headrow1) 생성 및 스타일 적용
                    	    HSSFCell cell1 = headrow1.createCell(0);
                    	    cell1.setCellValue("none".equals(record.getInformerName()) ? "" : record.getInformerName()); // 이름 입력
                    	    cell1.setCellStyle(cellStyle1); // 스타일 적용

                    	    // 통신원 셀 1 (headrow1) 생성
                    	    HSSFCell cell2 = headrow1.createCell(1);
                    	    cell2.setCellValue("통신원님 귀하");
                    	    cell2.setCellStyle(cellStyle2); // 스타일 적용

                    	    // 전화 번호 셀 0 (headrow2) 생성 및 스타일 적용
                    	    HSSFCell cell3 = headrow2.createCell(0);
                    	    cell3.setCellValue("none".equals(record.getPhoneCell()) ? "" : record.getPhoneCell()); // 전화 번호 입력
                    	    cell3.setCellStyle(cellStyle3); // 스타일 적용

                    	    // 우편번호 셀 1 (headrow2) 생성
                    	    HSSFCell cell4 = headrow2.createCell(1);
                    	    cell4.setCellValue("none".equals(record.getZipcode()) ? "" : record.getZipcode()); // 우편번호 입력
                    	    cell4.setCellStyle(cellStyle4); // 스타일 적용

                    	    // 시트 설정
                    	    sheet.setColumnWidth(0, 4000);
                    	    sheet.setColumnWidth(1, 4000);

                    	    headrow.setHeightInPoints(47.33f); // 행 높이 설정
                		} else {
                			break;
                		}
                		
                		sheet.setColumnWidth(2, 500);
                		
                		if((cellRowCnt+1) < dataCnt){
                			Object pickRecord2 = dataList.get(cellRowCnt+1);
                    		InfrmVO record2 = (InfrmVO) pickRecord2;
                    		
                    		// 폰트 및 스타일 설정
                    	    CellStyle cellStyle = wb.createCellStyle();
                    	    CellStyle cellStyle1 = wb.createCellStyle();
                    	    CellStyle cellStyle2 = wb.createCellStyle();
                    	    CellStyle cellStyle3 = wb.createCellStyle();
                    	    CellStyle cellStyle4 = wb.createCellStyle();
                    	    
                    	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
                    	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	  
                    	    cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    Font font = wb.createFont();
                    	    font.setFontHeightInPoints((short) 12); // 폰트 크기 12px로 설정
                    	    cellStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

                    	    
                    	    Font font1 = wb.createFont();
                    	    font1.setFontHeightInPoints((short) 14);
                    	    font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle1.setFont(font1); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    Font font2 = wb.createFont();
                    	    font2.setFontHeightInPoints((short) 10);
                    	    cellStyle2.setFont(font2); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    
                    	    Font font3 = wb.createFont();
                    	    font3.setFontHeightInPoints((short) 11);
                    	    font3.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle3.setFont(font3); 

                    	    Font font4 = wb.createFont();
                    	    font4.setFontHeightInPoints((short) 12);
                    	    font4.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle4.setFont(font4); // 폰트 스타일을 셀 스타일에 적용
                    	 
                    		
                    	 // 주소 셀 0 (headrow) 생성 및 스타일 적용
                    	    HSSFCell cell0 = headrow.createCell(3);
                    	    cell0.setCellValue("none".equals(record2.getAddressHome()) ? "" : record2.getAddressHome()); // 주소 입력
                    	    cell0.setCellStyle(cellStyle); // 스타일 적용
                    	    sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 3, 4)); // 주소 입력 란 셀 병합

                    	    // 이름 셀 0 (headrow1) 생성 및 스타일 적용
                    	    HSSFCell cell1 = headrow1.createCell(3);
                    	    cell1.setCellValue("none".equals(record2.getInformerName()) ? "" : record2.getInformerName()); // 이름 입력
                    	    cell1.setCellStyle(cellStyle1); // 스타일 적용

                    	    // 통신원 셀 1 (headrow1) 생성
                    	    HSSFCell cell2 = headrow1.createCell(4);
                    	    cell2.setCellValue("통신원님 귀하");
                    	    cell2.setCellStyle(cellStyle2); // 스타일 적용

                    	    // 전화 번호 셀 0 (headrow2) 생성 및 스타일 적용
                    	    HSSFCell cell3 = headrow2.createCell(3);
                    	    cell3.setCellValue("none".equals(record2.getPhoneCell()) ? "" : record2.getPhoneCell()); // 전화 번호 입력
                    	    cell3.setCellStyle(cellStyle3); // 스타일 적용

                    	    // 우편번호 셀 1 (headrow2) 생성
                    	    HSSFCell cell4 = headrow2.createCell(4);
                    	    cell4.setCellValue("none".equals(record2.getZipcode()) ? "" : record2.getZipcode()); // 우편번호 입력
                    	    cell4.setCellStyle(cellStyle4); // 스타일 적용

                    	    // 시트 설정
                    	    sheet.setColumnWidth(3, 4000);
                    	    sheet.setColumnWidth(4, 4000);

                    	    headrow.setHeightInPoints(47.33f); // 행 높이 설정
                		} else {
                			break;
                		}
                		
                		sheet.setColumnWidth(5, 500);
                		
                		if((cellRowCnt+2) < dataCnt){
                			Object pickRecord3 = dataList.get(cellRowCnt+2);
                    		InfrmVO record3 = (InfrmVO) pickRecord3;
                    		
                    		// 폰트 및 스타일 설정
                    	    CellStyle cellStyle = wb.createCellStyle();
                    	    CellStyle cellStyle1 = wb.createCellStyle();
                    	    CellStyle cellStyle2 = wb.createCellStyle();
                    	    CellStyle cellStyle3 = wb.createCellStyle();
                    	    CellStyle cellStyle4 = wb.createCellStyle();
                    	    
                    	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 가운데 정렬 (가로 기준)
                    	    cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    cellStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 가운데 정렬 (가로 기준)
                    	    cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	  
                    	    cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
                    	    cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
                    	    
                    	    Font font = wb.createFont();
                    	    font.setFontHeightInPoints((short) 12); // 폰트 크기 12px로 설정
                    	    cellStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용

                    	    
                    	    Font font1 = wb.createFont();
                    	    font1.setFontHeightInPoints((short) 14);
                    	    font1.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle1.setFont(font1); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    Font font2 = wb.createFont();
                    	    font2.setFontHeightInPoints((short) 10);
                    	    cellStyle2.setFont(font2); // 폰트 스타일을 셀 스타일에 적용
                    	    
                    	    
                    	    Font font3 = wb.createFont();
                    	    font3.setFontHeightInPoints((short) 11);
                    	    font3.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle3.setFont(font3); 

                    	    Font font4 = wb.createFont();
                    	    font4.setFontHeightInPoints((short) 12);
                    	    font4.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
                    	    cellStyle4.setFont(font4); // 폰트 스타일을 셀 스타일에 적용
                    	 
                    		
                    	 // 주소 셀 0 (headrow) 생성 및 스타일 적용
                    	    HSSFCell cell0 = headrow.createCell(6);
                    	    cell0.setCellValue("none".equals(record3.getAddressHome()) ? "" : record3.getAddressHome()); // 주소 입력
                    	    cell0.setCellStyle(cellStyle); // 스타일 적용
                    	    sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 6, 7)); // 주소 입력 란 셀 병합

                    	    // 이름 셀 0 (headrow1) 생성 및 스타일 적용
                    	    HSSFCell cell1 = headrow1.createCell(6);
                    	    cell1.setCellValue("none".equals(record3.getInformerName()) ? "" : record3.getInformerName()); // 이름 입력
                    	    cell1.setCellStyle(cellStyle1); // 스타일 적용

                    	    // 통신원 셀 1 (headrow1) 생성
                    	    HSSFCell cell2 = headrow1.createCell(7);
                    	    cell2.setCellValue("통신원님 귀하");
                    	    cell2.setCellStyle(cellStyle2); // 스타일 적용

                    	    // 전화 번호 셀 0 (headrow2) 생성 및 스타일 적용
                    	    HSSFCell cell3 = headrow2.createCell(6);
                    	    cell3.setCellValue("none".equals(record3.getPhoneCell()) ? "" : record3.getPhoneCell()); // 전화 번호 입력
                    	    cell3.setCellStyle(cellStyle3); // 스타일 적용

                    	    // 우편번호 셀 1 (headrow2) 생성
                    	    HSSFCell cell4 = headrow2.createCell(7);
                    	    cell4.setCellValue("none".equals(record3.getZipcode()) ? "" : record3.getZipcode()); // 우편번호 입력
                    	    cell4.setCellStyle(cellStyle4); // 스타일 적용

                    	    // 시트 설정
                    	    sheet.setColumnWidth(6, 4000);
                    	    sheet.setColumnWidth(7, 4000);

                    	    headrow.setHeightInPoints(47.33f); // 행 높이 설정
                		} else {
                			break;
                		}
                		
              		
                	}
            		
                	rowCnt = rowCnt + 3;
                	
                	cellRowCnt = cellRowCnt + 3;
                	
                }
                
                // 값 초기화
                rowCnt = 0;
    		}
    		
    	}
    }
    
    
    
    // 시간대별 교통정보 제공대장
    public void standardInformerTypeTime(Map model, HSSFWorkbook wb) {
    	List timeDataList = (List) model.get("timeData"); // 시간별 데이터
    	List ifrmDataList = (List) model.get("ifrmData"); // 제보자별 데이터
    	List rptTataList = (List) model.get("rptTData"); // 제보유형별 데이터
    	List rptMDataList = (List) model.get("rptMData"); // 제보수단별 데이터
    	
    	// 전체 건수 구하기
    	int allSum = 0;
    	
    	// sheet 생성 및 이름 저장
    	HSSFSheet sheet = wb.createSheet("시간대별 교통정보 제공대장");
         
        // title style 변경 
        // style 객체 생성
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 30); // 폰트 크기 설정
 	    font.setColor(IndexedColors.BLUE.getIndex()); // 폰트 색상 설정 => 예제 파일 기준으로 파란색 설정
 	    font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 볼드체로 설정
 	    font.setFontName("굴림체");
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
 	    // 엑셀 title 넣을 행 생성 (A1)
    	HSSFRow titlerow = sheet.createRow(0);
        
        HSSFCell titleCell = titlerow.createCell(0); // 셀 생성
        
        // CellRangeAddress(a,b,c,d) 사용법
        // => CellRangeAddress(첫 행,마지막 행,첫 열,마지막 열)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
        
        titleCell.setCellValue(model.get("titleName").toString()); // title row에 title 삽입
        titleCell.setCellStyle(titleStyle); // 스타일 적용
        
 	    // cell width ,  height 변경
 	    sheet.setColumnWidth(0, 5700); // setColumnWidth(변경 할 cell , 크기)
 	    titlerow.setHeightInPoints(47.33f); //setHeightInPoints 크기)
 	    
 	    // 엑셀 대제목 끝 ========================================================= //
 	    
        // title style 변경 
        // style 객체 생성
        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 오른쪽 정렬 (가로 기준)
        dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font dateFont = wb.createFont(); // 폰트 객체 생성
 	    dateFont.setFontHeightInPoints((short) 16); // 폰트 크기 설정
 	    dateFont.setColor(IndexedColors.BLUE.getIndex()); // 폰트 색상 설정 => 예제 파일 기준으로 파란색 설정
 	    dateFont.setFontName("굴림체");
 	    dateStyle.setFont(dateFont); // 폰트 스타일을 셀 스타일에 적용
 	   
    	// 시작일, 종료일 YYYY년 DD월 MM일 ~  YYYY년 DD월 MM일 형식으로 쪼개기      
    	// 쪼개기 위해 시작일과 종료일 받아오기
    	String startDate = model.get("start_date").toString();
    	String endDate = model.get("end_date").toString();
    	
    	String sDate = ( startDate.substring(0, 4) + "년 " + startDate.substring(4, 6) +"월 " + startDate.substring(6, 8) + "일 ~ ");
    	String eDate = ( endDate.substring(0, 4) + "년 " + endDate.substring(4, 6) +"월 " + endDate.substring(6, 8) + "일" );
    	
    	String dateStr = sDate + eDate; // 최종 반환 String
    	
    	
 	    // 기간 넣을 행 생성 (A2)
    	HSSFRow daterow = sheet.createRow(1);	
    	HSSFCell dateCell = daterow.createCell(0);
    	
    	sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 16)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
    	dateCell.setCellValue(dateStr); // title row에 title 삽입
    	dateCell.setCellStyle(dateStyle); // 스타일 적용
    	
    	// cell width ,  height 변경
 	    sheet.setColumnWidth(0, 5700); // setColumnWidth(변경 할 cell , 크기)
 	    daterow.setHeightInPoints(27.88f); //setHeightInPoints 크기)
    	
    	
 	    // 기간 끝 ========================================================= //
 	    // 시간 배열
 	    
	 	CellStyle dayTypehead = wb.createCellStyle();
	 	dayTypehead.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
	 	dayTypehead.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	 	dayTypehead.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // 배경색 설정
	 	dayTypehead.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
	 	dayTypehead.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
	 	dayTypehead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	 	dayTypehead.setBorderTop(HSSFCellStyle.BORDER_THIN);
	 	dayTypehead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	 	
	 	Font dayTypeF = wb.createFont(); // 폰트 객체 생성
	 	dayTypeF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
	 	dayTypeF.setFontName("굴림체");
	 	dayTypehead.setFont(dayTypeF); // 폰트 스타일을 셀 스타일에 적용
	 	
	 	
	 	// 구분 , 건수,비율,캐스터,스튜디오 등등의 > 스타일
	 		CellStyle gubun = wb.createCellStyle();
	 	   gubun.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
	 	   gubun.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	 	   /*gubun.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // 배경색 설정
	*/ 	   /*gubun.setFillPattern(CellStyle.SOLID_FOREGROUND);*/  // 배경색이 채워지도록 패턴 설정
	 	   gubun.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
	 	   gubun.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	 	   gubun.setBorderTop(HSSFCellStyle.BORDER_THIN);
	 	   gubun.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 	
		 	Font gubunF = wb.createFont(); // 폰트 객체 생성
		 	gubunF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		 	gubunF.setFontName("굴림체");
		 	gubun.setFont(gubunF); // 폰트 스타일을 셀 스타일에 적용
		 	
		 	DataFormat format = wb.createDataFormat();
		 	
		// 오전, 오후 표 안 데이터 스타일
		 	CellStyle dataStyle = wb.createCellStyle();  
		 	dataStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
		 	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		 	dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); 
		 	dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 	dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 	dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 	dataStyle.setDataFormat(format.getFormat("#,##0"));
		 	
		Font dataStyleF = wb.createFont(); // 폰트 객체 생성
		dataStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyleF.setFontName("굴림체");
		dataStyle.setFont(dataStyleF); // 폰트 스타일을 셀 스타일에 적용 	
		 	
		 	
		 	
		// 총 접수 건수, 자체 처리, 방송요청
		CellStyle allStyle = wb.createCellStyle();
		allStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
		allStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		allStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
		allStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		allStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		allStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		allStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		allStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		 	
		Font allStyleF = wb.createFont(); // 폰트 객체 생성
		allStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		allStyleF.setFontName("굴림체");
	 	allStyle.setFont(allStyleF); // 폰트 스타일을 셀 스타일에 적용 	
		
		// 방송, 비방송, 주요제보 등
		CellStyle otherStyle = wb.createCellStyle();
		otherStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
		otherStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		otherStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex()); // 배경색 설정*/ 	   
		otherStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		otherStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		otherStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		otherStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		otherStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font otherStyleF = wb.createFont(); // 폰트 객체 생성
		otherStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		otherStyleF.setFontName("굴림체");
		otherStyle.setFont(otherStyleF); // 폰트 스타일을 셀 스타일에 적용 	
		
	 	
 	    String[] daytimeArr = new String[28];
 	    daytimeArr[0] = "계";
 	    daytimeArr[1] = "오전";
 	    daytimeArr[2] = "00-01";
	 	daytimeArr[3] = "01-02";	
	 	daytimeArr[4] = "02-03";
	 	daytimeArr[5] = "03-04";
	 	daytimeArr[6] = "04-05";
	 	daytimeArr[7] = "05-06";
	 	daytimeArr[8] = "06-07";
	 	daytimeArr[9] = "07-08";
	 	daytimeArr[10] = "08-09";
	 	daytimeArr[11] = "09-10";
	 	daytimeArr[12] = "10-11";
	 	daytimeArr[13] = "11-12";
	 	daytimeArr[14] = "계";
	 	daytimeArr[15] = "오후";
	 	daytimeArr[16] = "12-13";
	 	daytimeArr[17] = "13-14";
	 	daytimeArr[18] = "14-15";
	 	daytimeArr[19] = "15-16";
	 	daytimeArr[20] = "16-17";
	 	daytimeArr[21] = "17-18";
	 	daytimeArr[22] = "18-19";
	 	daytimeArr[23] = "19-20";
	 	daytimeArr[24] = "20-21";
	 	daytimeArr[25] = "21-22";
	 	daytimeArr[26] = "22-23";
	 	daytimeArr[27] = "23-24";
 	    
	 	HSSFRow dayTyperow = sheet.createRow(5);  // 오전 구분 (A6)
	 	HSSFRow dayallrow = sheet.createRow(6); // 오전 총 접수 건수
	 	
	 	HSSFRow nightTyperow = sheet.createRow(28);  // 오후 구분 (A29)
	 	HSSFRow nightallrow = sheet.createRow(29); // 오후 총 접수 건수

	 	HSSFCell dayTypeCell = dayTyperow.createCell(0); // 구분(오전)
	 	HSSFCell dayallCell = dayallrow.createCell(0); // 오전 총 접수 건수
 	    sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2)); // cell 병합 / 예제 파일 기준으로3칸 병합
 	    sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 2)); // cell 병합 / 예제 파일 기준으로3칸 병합
 	    dayTypeCell.setCellValue("구분"); // title row에 title 삽입
 	    dayallCell.setCellValue("총 접수 건수"); // title row에 title 삽입
 	    dayTypeCell.setCellStyle(gubun);
 	    dayTyperow.createCell(1).setCellStyle(gubun);
 	    dayTyperow.createCell(2).setCellStyle(gubun);
 	    dayallCell.setCellStyle(allStyle);
 	    dayallrow.createCell(1).setCellStyle(allStyle);
 	 	dayallrow.createCell(2).setCellStyle(allStyle);

 	    HSSFCell nightTypeCell = nightTyperow.createCell(0); // 구분(오후)
 	    HSSFCell nightallCell = nightallrow.createCell(0); // 오전 총 접수 건수
 	    sheet.addMergedRegion(new CellRangeAddress(28, 28, 0, 2)); // cell 병합 / 예제 파일 기준으로3칸 병합
 	    sheet.addMergedRegion(new CellRangeAddress(29, 29, 0, 2)); // cell 병합 / 예제 파일 기준으로3칸 병합
 	    nightallCell.setCellValue("총 접수 건수"); // title row에 title 삽입
 	    nightallCell.setCellStyle(allStyle);
 	    nightallrow.createCell(1).setCellStyle(allStyle);
 	    nightallrow.createCell(2).setCellStyle(allStyle);
 	    
 	    // 자체 처리 / 방송 건수
 	    HSSFRow daysendcnt = sheet.createRow(7); 
 	    HSSFRow daysendper = sheet.createRow(8);  
 	    HSSFRow daybrodcnt = sheet.createRow(9);  
 	    HSSFRow daybrodper = sheet.createRow(10);

 	   HSSFCell daysendcntcell = daysendcnt.createCell(0); 
 	   sheet.addMergedRegion(new CellRangeAddress(7, 8, 0, 1)); // cell 병합 
 	   daysendcntcell.setCellValue("자체 처리");
 	   daysendcntcell.setCellStyle(allStyle);
 	   daysendcnt.createCell(1).setCellStyle(allStyle);
 	   daysendper.createCell(0).setCellStyle(allStyle);
 	   daysendper.createCell(1).setCellStyle(allStyle);
 	   
 	   HSSFCell daysendcnttitle = daysendcnt.createCell(2);
 	   daysendcnttitle.setCellValue("건수");
 	   daysendcnttitle.setCellStyle(gubun);
 	  
	   HSSFCell daysendpertitle = daysendper.createCell(2);
	   daysendpertitle.setCellValue("비율"); 
	   daysendpertitle.setCellStyle(gubun);
	   
	   HSSFCell daybrodcntcell = daybrodcnt.createCell(0); 
 	   sheet.addMergedRegion(new CellRangeAddress(9, 10, 0, 1)); // cell 병합 
 	   daybrodcntcell.setCellValue("방송 요청");
 	   daybrodcntcell.setCellStyle(allStyle);
 	   daybrodcnt.createCell(1).setCellStyle(allStyle);
 	   daybrodper.createCell(0).setCellStyle(allStyle);
 	   daybrodper.createCell(1).setCellStyle(allStyle);
 	   
 	   HSSFCell daybrodcnttitle = daybrodcnt.createCell(2);
 	   daybrodcnttitle.setCellValue("건수");
 	   daybrodcnttitle.setCellStyle(gubun);
 	  
	   HSSFCell daybrodpertitle = daybrodper.createCell(2);
	   daybrodpertitle.setCellValue("비율");  
	   daybrodpertitle.setCellStyle(gubun);
	   
 	
 	    HSSFRow nightsendcnt = sheet.createRow(30); 
	    HSSFRow nightsendper = sheet.createRow(31);  
	    HSSFRow nightbrodcnt = sheet.createRow(32);  
	    HSSFRow nightbrodper = sheet.createRow(33);
	    
	    HSSFCell nightsendcntcell = nightsendcnt.createCell(0); 
	 	sheet.addMergedRegion(new CellRangeAddress(30, 31, 0, 1)); // cell 병합 
	 	nightsendcntcell.setCellValue("자체 처리");  
	 	nightsendcntcell.setCellStyle(allStyle);
	 	nightsendcnt.createCell(1).setCellStyle(allStyle);
	 	
	 	HSSFCell nightsendcnttitle = nightsendcnt.createCell(2);
	 	nightsendcnttitle.setCellValue("건수");
	 	nightsendcnttitle.setCellStyle(gubun);
	 	
		HSSFCell nightsendpertitle = nightsendper.createCell(2);
		nightsendpertitle.setCellValue("비율"); 
		nightsendpertitle.setCellStyle(gubun);
		
		HSSFCell nightbrodcntcell = nightbrodcnt.createCell(0); 
	 	sheet.addMergedRegion(new CellRangeAddress(32, 33, 0, 1)); // cell 병합 
	 	nightbrodcntcell.setCellValue("방송 요청");
	 	nightbrodcntcell.setCellStyle(allStyle);
	 	nightbrodcnt.createCell(1).setCellStyle(allStyle);
	 	nightbrodper.createCell(0).setCellStyle(allStyle);
	 	nightbrodper.createCell(1).setCellStyle(allStyle);
	 	
	 	HSSFCell nightbrodcnttitle = nightbrodcnt.createCell(2);
	 	nightbrodcnttitle.setCellValue("건수");
	 	nightbrodcnttitle.setCellStyle(gubun);
	 	
		HSSFCell nightbrodpertitle = nightbrodper.createCell(2);
		nightbrodpertitle.setCellValue("비율"); 
		nightbrodpertitle.setCellStyle(gubun);
		
		
		HSSFRow dayybroadcnt = sheet.createRow(12); 
 	    HSSFRow dayybroadper = sheet.createRow(13);  
 	    HSSFRow dayybroadstudio = sheet.createRow(14);  
 	    HSSFRow dayybroadcaster = sheet.createRow(15);
 	    HSSFRow daynbroadcnt = sheet.createRow(16);  
	    HSSFRow daynbroadper = sheet.createRow(17);
	    
	    sheet.addMergedRegion(new CellRangeAddress(12, 15, 1, 1));
	    sheet.addMergedRegion(new CellRangeAddress(16, 17, 1, 1));
	    
	    HSSFCell dayybroadcntcell = dayybroadcnt.createCell(1); 
	    HSSFCell daynbroadcntcell = daynbroadcnt.createCell(1); 
	    dayybroadcntcell.setCellValue("방송");
	    daynbroadcntcell.setCellValue("비방송");
	    dayybroadcntcell.setCellStyle(otherStyle);
	    daynbroadcntcell.setCellStyle(otherStyle);
	    
	    dayybroadper.createCell(1).setCellStyle(otherStyle);
	    dayybroadstudio.createCell(1).setCellStyle(otherStyle);
	    dayybroadcaster.createCell(1).setCellStyle(otherStyle);
	    daynbroadper.createCell(1).setCellStyle(otherStyle);
	    
	    HSSFCell dayybroadcnttitle = dayybroadcnt.createCell(2);
	    dayybroadcnttitle.setCellValue("건수");
	    dayybroadcnttitle.setCellStyle(gubun);
	    
		HSSFCell dayybroadpertitle = dayybroadper.createCell(2);
		dayybroadpertitle.setCellValue("비율"); 
		dayybroadpertitle.setCellStyle(gubun);
		
		HSSFCell dayybroadstudiotitle = dayybroadstudio.createCell(2);
		dayybroadstudiotitle.setCellValue("STUDIO");
		dayybroadstudiotitle.setCellStyle(gubun);
		
		HSSFCell dayybroadcastertitle = dayybroadcaster.createCell(2);
		dayybroadcastertitle.setCellValue("CASTER"); 
		dayybroadcastertitle.setCellStyle(gubun);
		
		HSSFCell daynbroadcnttitle = daynbroadcnt.createCell(2);
		daynbroadcnttitle.setCellValue("건수");
		daynbroadcnttitle.setCellStyle(gubun);
		
		HSSFCell daynbroadpertitle = daynbroadper.createCell(2);
		daynbroadpertitle.setCellValue("비율"); 
		daynbroadpertitle.setCellStyle(gubun);
	    
	    HSSFRow nightybroadcnt = sheet.createRow(35); 
 	    HSSFRow nightybroadper = sheet.createRow(36);  
 	    HSSFRow nightybroadstudio = sheet.createRow(37);  
 	    HSSFRow nightybroadcaster = sheet.createRow(38);
 	    HSSFRow nightnbroadcnt = sheet.createRow(39);  
	    HSSFRow nightnbroadper = sheet.createRow(40);
	    
	    sheet.addMergedRegion(new CellRangeAddress(35, 38, 1, 1));
	    sheet.addMergedRegion(new CellRangeAddress(39, 40, 1, 1));

	    HSSFCell nightybroadcntcell = nightybroadcnt.createCell(1); 
	    HSSFCell nightnbroadcntcell = nightnbroadcnt.createCell(1); 
	    nightybroadcntcell.setCellValue("방송");
	    nightnbroadcntcell.setCellValue("비방송");
	    nightybroadcntcell.setCellStyle(otherStyle);
	    nightnbroadcntcell.setCellStyle(otherStyle);
	    nightybroadcntcell.setCellStyle(otherStyle);
	    nightnbroadcntcell.setCellStyle(otherStyle);
	    
	    nightybroadper.createCell(1).setCellStyle(otherStyle);
	    nightybroadstudio.createCell(1).setCellStyle(otherStyle);
	    nightybroadcaster.createCell(1).setCellStyle(otherStyle);
	    nightnbroadper.createCell(1).setCellStyle(otherStyle);
	    
	    HSSFCell nightybroadcnttitle = nightybroadcnt.createCell(2);
	    nightybroadcnttitle.setCellValue("건수");
	    nightybroadcnttitle.setCellStyle(gubun);
	    
		HSSFCell nightybroadpertitle = nightybroadper.createCell(2);
		nightybroadpertitle.setCellValue("비율"); 
		nightybroadpertitle.setCellStyle(gubun);
		
		HSSFCell nightybroadstudiotitle = nightybroadstudio.createCell(2);
		nightybroadstudiotitle.setCellValue("STUDIO");
		nightybroadstudiotitle.setCellStyle(gubun);
		
		HSSFCell nightybroadcastertitle = nightybroadcaster.createCell(2);
		nightybroadcastertitle.setCellValue("CASTER"); 
		nightybroadcastertitle.setCellStyle(gubun);
		
		HSSFCell nightnbroadcnttitle = nightnbroadcnt.createCell(2);
		nightnbroadcnttitle.setCellValue("건수");
		nightnbroadcnttitle.setCellStyle(gubun);
		
		HSSFCell nightnbroadpertitle = nightnbroadper.createCell(2);
		nightnbroadpertitle.setCellValue("비율"); 
		nightnbroadpertitle.setCellStyle(gubun);
		
	    
	    HSSFRow dayicnt = sheet.createRow(19); 
 	    HSSFRow dayiybcnt = sheet.createRow(20);  
 	    HSSFRow dayiybroadper = sheet.createRow(21);  
 	    HSSFRow dayistudio = sheet.createRow(22);
 	    HSSFRow dayicaster = sheet.createRow(23);  

 	    HSSFRow nighticnt = sheet.createRow(42); 
	    HSSFRow nightiybcnt = sheet.createRow(43);  
	    HSSFRow nightiybroadper = sheet.createRow(44);  
	    HSSFRow nightistudio = sheet.createRow(45);
	    HSSFRow nighticaster = sheet.createRow(46); 
	    
	    sheet.addMergedRegion(new CellRangeAddress(19, 23, 1, 1));
	    sheet.addMergedRegion(new CellRangeAddress(42, 46, 1, 1));
	    
	    HSSFCell dayicntcell = dayicnt.createCell(1); 
	    HSSFCell nighticntcell = nighticnt.createCell(1); 
	    dayicntcell.setCellValue("주요 제보");
	    nighticntcell.setCellValue("주요 제보");
	    dayicntcell.setCellStyle(otherStyle);
	    nighticntcell.setCellStyle(otherStyle);
	    dayiybcnt.createCell(1).setCellStyle(otherStyle);
	    dayiybroadper.createCell(1).setCellStyle(otherStyle);
	    dayistudio.createCell(1).setCellStyle(otherStyle);
	    dayicaster.createCell(1).setCellStyle(otherStyle);
	    nightiybcnt.createCell(1).setCellStyle(otherStyle);
	    nightiybroadper.createCell(1).setCellStyle(otherStyle);
	    nightistudio.createCell(1).setCellStyle(otherStyle);
	    nighticaster.createCell(1).setCellStyle(otherStyle);
	    
	    HSSFCell dayicnttitle = dayicnt.createCell(2);
	    dayicnttitle.setCellValue("전송 건수");
	    dayicnttitle.setCellStyle(gubun);
	    
		HSSFCell dayiybcnttitle = dayiybcnt.createCell(2);
		dayiybcnttitle.setCellValue("방송 건수"); 
		dayiybcnttitle.setCellStyle(gubun);
		
		HSSFCell dayiybroadpertitle = dayiybroadper.createCell(2);
		dayiybroadpertitle.setCellValue("방송 비율");
		dayiybroadpertitle.setCellStyle(gubun);
		
		HSSFCell dayistudiotitle = dayistudio.createCell(2);
		dayistudiotitle.setCellValue("STUDIO"); 
		dayistudiotitle.setCellStyle(gubun);
		
		HSSFCell dayicastertitle = dayicaster.createCell(2);
		dayicastertitle.setCellValue("CASTER");
		dayicastertitle.setCellStyle(gubun);

		
		HSSFCell nighticnttitle = nighticnt.createCell(2);
		nighticnttitle.setCellValue("전송 건수");
		nighticnttitle.setCellStyle(gubun);
		
		HSSFCell nightiybcnttitle = nightiybcnt.createCell(2);
		nightiybcnttitle.setCellValue("방송 건수");
		nightiybcnttitle.setCellStyle(gubun);
		
		HSSFCell nightiybroadpertitle = nightiybroadper.createCell(2);
		nightiybroadpertitle.setCellValue("방송 비율");
		nightiybroadpertitle.setCellStyle(gubun);
		
		HSSFCell nightistudiotitle = nightistudio.createCell(2);
		nightistudiotitle.setCellValue("STUDIO"); 
		nightistudiotitle.setCellStyle(gubun);
		
		HSSFCell nighticastertitle = nighticaster.createCell(2);
		nighticastertitle.setCellValue("CASTER");
		nighticastertitle.setCellStyle(gubun);
	    
		
		for(int i = 0 ; i < 28 ; i++) {
			
			if(i < 14) {
				HSSFCell dayTimeCell = dayTyperow.createCell(i + 3); // 계
	 	    	dayTimeCell.setCellValue(daytimeArr[i]); // title row에 title 삽입
	 	    	dayTimeCell.setCellStyle(dayTypehead); // 스타일 적용
			} else {
				if( i == 14 ) {
					continue;
				} else {
					HSSFCell nightTimeCell = nightTyperow.createCell(i - 11); // 계
					nightTimeCell.setCellValue(daytimeArr[i]); // title row에 title 삽입
					nightTimeCell.setCellStyle(dayTypehead); // 스타일 적용
				}
			}
 	    	
 	    }
		
		
		// 없는 값들을 위해 기본 값 깔기
		for(int i = 0 ; i < 14; i ++) {
			HSSFCell dayAllCell = dayallrow.createCell(i+3);
			dayAllCell.setCellValue(0);
			dayAllCell.setCellStyle(dataStyle);

			HSSFCell nightAllCell = nightallrow.createCell(i+3);
			nightAllCell.setCellValue(0);
			nightAllCell.setCellStyle(dataStyle);

			HSSFCell daysendcntCell = daysendcnt.createCell(i+3);
			HSSFCell daysendperCell = daysendper.createCell(i+3);
			HSSFCell daybrodcntCell = daybrodcnt.createCell(i+3);
			HSSFCell daybrodperCell = daybrodper.createCell(i+3);
			
			daysendcntCell.setCellValue(0);
			daysendperCell.setCellValue(0 + "%");
			daybrodcntCell.setCellValue(0);
			daybrodperCell.setCellValue(0 + "%");
			
			daysendcntCell.setCellStyle(dataStyle);
			daysendperCell.setCellStyle(dataStyle);
			daybrodcntCell.setCellStyle(dataStyle);
			daybrodperCell.setCellStyle(dataStyle);
			
			HSSFCell nightsendcntCell = nightsendcnt.createCell(i+3);
			HSSFCell nightsendperCell = nightsendper.createCell(i+3);
			HSSFCell nightbrodcntCell = nightbrodcnt.createCell(i+3);
			HSSFCell nightbrodperCell = nightbrodper.createCell(i+3);
			
			nightsendcntCell.setCellValue(0);
			nightsendperCell.setCellValue(0 + "%");
			nightbrodcntCell.setCellValue(0);
			nightbrodperCell.setCellValue(0 + "%");
			
			nightsendcntCell.setCellStyle(dataStyle);
			nightsendperCell.setCellStyle(dataStyle);
			nightbrodcntCell.setCellStyle(dataStyle);
			nightbrodperCell.setCellStyle(dataStyle);
		    
		    HSSFCell dayybroadcntCell = dayybroadcnt.createCell(i+3);
			HSSFCell dayybroadperCell = dayybroadper.createCell(i+3);
			HSSFCell dayybroadstudioCell = dayybroadstudio.createCell(i+3);
			HSSFCell dayybroadcasterCell = dayybroadcaster.createCell(i+3);
			HSSFCell daynbroadcntCell = daynbroadcnt.createCell(i+3);
			HSSFCell daynbroadperCell = daynbroadper.createCell(i+3);
			
			dayybroadcntCell.setCellValue(0);
			dayybroadperCell.setCellValue(0 + "%");
			dayybroadstudioCell.setCellValue(0);
			dayybroadcasterCell.setCellValue(0);
			daynbroadcntCell.setCellValue(0);
			daynbroadperCell.setCellValue(0 + "%");
			
			dayybroadcntCell.setCellStyle(dataStyle);
			dayybroadperCell.setCellStyle(dataStyle);
			dayybroadstudioCell.setCellStyle(dataStyle);
			dayybroadcasterCell.setCellStyle(dataStyle);
			daynbroadcntCell.setCellStyle(dataStyle);
			daynbroadperCell.setCellStyle(dataStyle);
		
		    
		    HSSFCell nightybroadcntCell = nightybroadcnt.createCell(i+3);
			HSSFCell nightybroadperCell = nightybroadper.createCell(i+3);
			HSSFCell nightybroadstudioCell = nightybroadstudio.createCell(i+3);
			HSSFCell nightybroadcasterCell = nightybroadcaster.createCell(i+3);
			HSSFCell nightnbroadcntCell = nightnbroadcnt.createCell(i+3);
			HSSFCell nightnbroadperCell = nightnbroadper.createCell(i+3);
			
			nightybroadcntCell.setCellValue(0);
			nightybroadperCell.setCellValue(0 + "%");
			nightybroadstudioCell.setCellValue(0);
			nightybroadcasterCell.setCellValue(0);
			nightnbroadcntCell.setCellValue(0);
			nightnbroadperCell.setCellValue(0 + "%");
			
			nightybroadcntCell.setCellStyle(dataStyle);
			nightybroadperCell.setCellStyle(dataStyle);
			nightybroadstudioCell.setCellStyle(dataStyle);
			nightybroadcasterCell.setCellStyle(dataStyle);
			nightnbroadcntCell.setCellStyle(dataStyle);
			nightnbroadperCell.setCellStyle(dataStyle);

	 	   	
	 	   HSSFCell dayicntCell = dayicnt.createCell(i+3);
			HSSFCell dayiybcntCell = dayiybcnt.createCell(i+3);
			HSSFCell dayiybroadperCell = dayiybroadper.createCell(i+3);
			HSSFCell dayistudioCell = dayistudio.createCell(i+3);
			HSSFCell dayicasterCell = dayicaster.createCell(i+3);
			
			dayicntCell.setCellValue(0);
			dayiybcntCell.setCellValue(0);
			dayiybroadperCell.setCellValue(0 + "%");
			dayistudioCell.setCellValue(0);
			dayicasterCell.setCellValue(0);
			
			dayicntCell.setCellStyle(dataStyle);
			dayiybcntCell.setCellStyle(dataStyle);
			dayiybroadperCell.setCellStyle(dataStyle);
			dayistudioCell.setCellStyle(dataStyle);
			dayicasterCell.setCellStyle(dataStyle);
	 	   
	 	   
	 	  HSSFCell nighticntCell = nighticnt.createCell(i+3);
			HSSFCell nightiybcntCell = nightiybcnt.createCell(i+3);
			HSSFCell nightiybroadperCell = nightiybroadper.createCell(i+3);
			HSSFCell nightistudioCell = nightistudio.createCell(i+3);
			HSSFCell nighticasterCell = nighticaster.createCell(i+3);
			
			nighticntCell.setCellValue(0);
			nightiybcntCell.setCellValue(0);
			nightiybroadperCell.setCellValue(0 + "%");
			nightistudioCell.setCellValue(0);
			nighticasterCell.setCellValue(0);
			
			nighticntCell.setCellStyle(dataStyle);
			nightiybcntCell.setCellStyle(dataStyle);
			nightiybroadperCell.setCellStyle(dataStyle);
			nightistudioCell.setCellStyle(dataStyle);
			nighticasterCell.setCellStyle(dataStyle);
		}
		
		nightallrow.createCell(3).setCellValue("");
		nightsendcnt.createCell(3).setCellValue("");
		nightsendper.createCell(3).setCellValue("");
		nightbrodcnt.createCell(3).setCellValue("");
		nightbrodper.createCell(3).setCellValue("");
		nightybroadcnt.createCell(3).setCellValue("");
	    nightybroadper.createCell(3).setCellValue("");
	    nightybroadstudio.createCell(3).setCellValue("");
	    nightybroadcaster.createCell(3).setCellValue("");
	    nightnbroadcnt.createCell(3).setCellValue("");
	    nightnbroadper.createCell(3).setCellValue("");
	    nighticnt.createCell(3).setCellValue("");
	 	nightiybcnt.createCell(3).setCellValue("");
	 	nightiybroadper.createCell(3).setCellValue("");
	 	nightistudio.createCell(3).setCellValue("");
	 	nighticaster.createCell(3).setCellValue("");
	    
	 	int dayAllsum = 0; // 오전 총 건수
	 	int nightAllsum = 0; // 오후 총 건수
	 	int daySendSum = 0;
	 	int nightSendSum = 0;
	 	double daySendper = 0;
	 	double nightSendper = 0;
	 	int dayBroadsum = 0;
	 	int nightBroadSum = 0;
	 	double dayBrodper = 0;
	 	double nightBrodper = 0;
	 	int dayBroadysum = 0;
	 	int nightBroadysum = 0;
	 	double dayBrodyper = 0; // 방송 비율 (방송요청 아님)
	 	double nightBrodyper = 0;
	 	int dayBroadys = 0;
	 	int dayBroadyc = 0;
	 	int nightBroadys = 0;
	 	int nightBroadyc = 0;
	 	int dayBrodnsum = 0;
	 	int nightBrodnsum = 0;
	 	double dayBrodnper = 0; // 방송 비율 (방송요청 아님)
	 	double nightBrodnper = 0;
	 	    int dayisum = 0;
	 	    int nightisum = 0;
	 	    int dayiybsum = 0; 
	 	    int nightiybsum = 0;
	 	    
	 	    double dayiybper = 0; // 주요 제보 방송 비율
	 	   double nightiybper = 0;
	 	   
	 	    int dayissum = 0;
	 	    int nightissum = 0;
	 	    int dayicsum = 0;
	 	    int nighticsum = 0;
	 	for(int i=0; i < timeDataList.size(); i ++) {
	 		RecordDto record = (RecordDto) timeDataList.get(i);
	 		String receiptTime = record.getString("RECEIPT_TIME");
	 		receiptTime = receiptTime.trim();
	 		int rcpTime = Integer.parseInt(receiptTime);
	 		
	 		for(int j=0; j < 24; j++) {
	 			if(rcpTime == j) {
		 			if(j < 12) { //오전
		 				
		 				
		 				HSSFCell dayallrowCell = dayallrow.createCell(j+5);
		 				dayallrowCell.setCellValue(record.getInt("ALL_CNT") + record.getInt("O_ALL_CNT")); // 총 접수 건수
		 				dayallrowCell.setCellStyle(dataStyle);
		 				dayAllsum = dayAllsum + record.getInt("ALL_CNT") + record.getInt("O_ALL_CNT");
		 				
		 				HSSFCell daysendcntCell = daysendcnt.createCell(j+5);
		 				daysendcntCell.setCellValue(record.getInt("SEND_N") + record.getInt("O_SEND_N")); // 자체처리 건수
		 				daysendcntCell.setCellStyle(dataStyle);
		 				daySendSum = daySendSum + record.getInt("SEND_N") + record.getInt("O_SEND_N");
		 				
		 				HSSFCell daysendperCell = daysendper.createCell(j + 5);
		 				double hap = ((record.getInt("SEND_N") + record.getInt("O_SEND_N")) / ((double) record.getInt("ALL_CNT") + (double) record.getInt("O_ALL_CNT")) ) * 100;
		 				hap = Math.round(hap * 10.0) / 10.0;
		 				daysendperCell.setCellValue(hap + "%");
		 				daysendperCell.setCellStyle(dataStyle);
		 				daySendper = daySendper + hap;
		 					 		
		 				HSSFCell daybrodcntCell = daybrodcnt.createCell(j+5);
		 				daybrodcntCell.setCellValue(record.getInt("SEND_Y") + record.getInt("O_SEND_Y")); // 방송요청 건수
		 				daybrodcntCell.setCellStyle(dataStyle);
		 				dayBroadsum =  dayBroadsum + record.getInt("SEND_Y") + record.getInt("O_SEND_Y");
		 						
		 				HSSFCell daybrodperCell =daybrodper.createCell(j + 5);
		 				double hap2 = ((record.getInt("SEND_Y") + record.getInt("O_SEND_Y")) / ((double) record.getInt("ALL_CNT") + (double) record.getInt("O_ALL_CNT"))) * 100;
		 				hap2 = Math.round(hap2 * 10.0) / 10.0;
		 				daybrodperCell.setCellValue(hap2 + "%");
		 				daybrodperCell.setCellStyle(dataStyle);
		 				dayBrodper = dayBrodper + hap2;
		 				
		 				HSSFCell dayybroadcntCell = dayybroadcnt.createCell(j+5);
		 				dayybroadcntCell.setCellValue(record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y")); // 방송 건수
		 				dayybroadcntCell.setCellStyle(dataStyle);
		 				dayBroadysum = dayBroadysum + record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y");
		 				
		 				HSSFCell dayybroadperCell = dayybroadper.createCell(j + 5);
		 				double hap3 = ((record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y")) / ((double) record.getInt("SEND_Y") + (double) record.getInt("O_SEND_Y")) )* 100;
		 				hap3 = Math.round(hap3 * 10.0) / 10.0;
		 				dayybroadperCell.setCellValue(hap3 + "%");
		 				dayybroadperCell.setCellStyle(dataStyle);
		 				dayBrodyper = dayBrodyper + hap3;
		 				
		 				HSSFCell dayybroadstudioCell = dayybroadstudio.createCell(j+5);
		 				dayybroadstudioCell.setCellValue(record.getInt("BROAD_C") + record.getInt("O_BROAD_C")); // 스튜디오 방송 건수
		 				dayybroadstudioCell.setCellStyle(dataStyle);
		 			   dayBroadys = dayBroadys + record.getInt("BROAD_C") + record.getInt("O_BROAD_C");
		 			   
		 			   
		 			  HSSFCell dayybroadcasterCell = dayybroadcaster.createCell(j+5);
		 			  dayybroadcasterCell.setCellValue(record.getInt("BROAD_P") + record.getInt("O_BROAD_P")); // 방송 건수 캐스터
		 			  dayybroadcasterCell.setCellStyle(dataStyle);
		 			  dayBroadyc = dayBroadyc + record.getInt("BROAD_P") + record.getInt("O_BROAD_P");
		 			  
		 			 HSSFCell daynbroadcntCell = daynbroadcnt.createCell(j+5);
		 			 daynbroadcntCell.setCellValue(record.getInt("BROAD_N") + record.getInt("O_BROAD_N")) ; // 비방송 건수
		 			 daynbroadcntCell.setCellStyle(dataStyle);
		 			   dayBrodnsum = dayBrodnsum + record.getInt("BROAD_N") + record.getInt("O_BROAD_N");
		 			   
		 			   
		 			  HSSFCell daynbroadperCell = daynbroadper.createCell(j + 5);
		 			 double hap4 = ((record.getInt("BROAD_N") + record.getInt("O_BROAD_N")) / ((double) record.getInt("SEND_Y") + (double) record.getInt("O_SEND_Y")) )* 100;
		 				hap4 = Math.round(hap4 * 10.0) / 10.0;
		 				daynbroadperCell.setCellValue(hap4 + "%");
		 				daynbroadperCell.setCellStyle(dataStyle);
		 				dayBrodnper = dayBrodnper + hap4;
		 			   
		 				HSSFCell dayicntCell = dayicnt.createCell(j+5);
		 				dayicntCell.setCellValue(record.getInt("IMP_Y") + record.getInt("O_IMP_Y")); // 전송 건수
		 				dayicntCell.setCellStyle(dataStyle);
		 		 	  dayisum = dayisum + record.getInt("IMP_Y") + record.getInt("O_IMP_Y");

		 		 	  
		 		 	HSSFCell dayiybcntCell = dayiybcnt.createCell(j+5);
		 		 	dayiybcntCell.setCellValue(record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y")); // 주요제보 방송건수
		 		 	dayiybcntCell.setCellStyle(dataStyle);
			 		 	dayiybsum = dayiybsum +record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y");
			 		 	
			 		 	
			 		 	HSSFCell dayiybroadperCell = dayiybroadper.createCell(j+5);
			 		 	double hap5 = ((record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y")) / ((double) record.getInt("IMP_Y") + (double) record.getInt("O_IMP_Y")) )* 100;
		 				hap5 = Math.round(hap5 * 10.0) / 10.0;
		 				dayiybroadperCell.setCellValue(hap5 + "%");
		 				dayiybroadperCell.setCellStyle(dataStyle);
		 				dayiybper = dayiybper + hap5;
		 				
		 				
		 				HSSFCell dayistudiorCell =dayistudio.createCell(j+5);
		 				dayistudiorCell.setCellValue(record.getInt("IMPBRO_P") + record.getInt("O_IMPBRO_P")); // 주요제보 스튜디오
		 				dayistudiorCell.setCellStyle(dataStyle);
			 		 	dayissum = dayissum +record.getInt("IMPBRO_P") + record.getInt("O_IMPBRO_P");
			 		 			
			 		 	HSSFCell dayicasterCell = dayicaster.createCell(j+5);
			 		 	dayicasterCell.setCellValue(record.getInt("IMPBRO_C") + record.getInt("O_IMPBRO_C")); // 주요제보 캐스터
			 		 	dayicasterCell.setCellStyle(dataStyle);
			 		 	dayicsum = dayicsum +record.getInt("IMPBRO_C") + record.getInt("O_IMPBRO_C");
			 		 	
			 		 	
			 		 	
			 		 	
		 			} else { //오후
		 				
		 				HSSFCell nightallrowCell = nightallrow.createCell(j-6);
		 				nightallrowCell.setCellValue(record.getInt("ALL_CNT") + record.getInt("O_ALL_CNT")); // 총 접수 건수
		 				nightallrowCell.setCellStyle(dataStyle);
		 				nightAllsum = nightAllsum + record.getInt("ALL_CNT") + record.getInt("O_ALL_CNT");
		 				
		 				HSSFCell nightsendcntCell = nightsendcnt.createCell(j-6);
		 				nightsendcntCell.setCellValue(record.getInt("SEND_N") + record.getInt("O_SEND_N")); // 자체처리 건수
		 				nightsendcntCell.setCellStyle(dataStyle);
		 				nightSendSum = nightSendSum + record.getInt("SEND_N") + record.getInt("O_SEND_N");
		 				
		 				HSSFCell nightsendperCell = nightsendper.createCell(j-6);
		 				double hap = ((record.getInt("SEND_N") + record.getInt("O_SEND_N")) / ((double) record.getInt("ALL_CNT") + (double) record.getInt("O_ALL_CNT"))) * 100;
		 				hap = Math.round(hap * 10.0) / 10.0;
		 				nightsendperCell.setCellValue(hap + "%");
		 				nightsendperCell.setCellStyle(dataStyle);
		 				nightSendper = nightSendper + hap;
		 					 		
		 				HSSFCell nightbrodcntCell = nightbrodcnt.createCell(j-6);
		 				nightbrodcntCell.setCellValue(record.getInt("SEND_Y") + record.getInt("O_SEND_Y")); // 방송요청 건수
		 				nightbrodcntCell.setCellStyle(dataStyle);
		 				nightBroadSum =  nightBroadSum + record.getInt("SEND_Y")+ record.getInt("O_SEND_Y");
		 						
		 				HSSFCell nightbrodperCell =nightbrodper.createCell(j-6);
		 				double hap2 = ((record.getInt("SEND_Y") + record.getInt("O_SEND_Y")) / ((double) record.getInt("ALL_CNT") + (double) record.getInt("O_ALL_CNT"))) * 100;
		 				hap2 = Math.round(hap2 * 10.0) / 10.0;
		 				nightbrodperCell.setCellValue(hap2 + "%");
		 				nightbrodperCell.setCellStyle(dataStyle);
		 				nightBrodper = nightBrodper + hap2;
		 				
		 				HSSFCell nightybroadcntCell = nightybroadcnt.createCell(j-6);
		 				nightybroadcntCell.setCellValue(record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y")); // 방송 건수
		 				nightybroadcntCell.setCellStyle(dataStyle);
		 				nightBroadysum = nightBroadysum + record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y");
		 				
		 				HSSFCell nightybroadperCell = nightybroadper.createCell(j-6);
		 				double hap3 = ((record.getInt("BROAD_Y") + record.getInt("O_BROAD_Y")) / ((double) record.getInt("SEND_Y") + (double) record.getInt("O_SEND_Y")) )* 100;
		 				hap3 = Math.round(hap3 * 10.0) / 10.0;
		 				nightybroadperCell.setCellValue(hap3 + "%");
		 				nightybroadperCell.setCellStyle(dataStyle);
		 				nightBrodyper = nightBrodyper + hap3;
		 				
		 				HSSFCell nightybroadstudioCell = nightybroadstudio.createCell(j-6);
		 				nightybroadstudioCell.setCellValue(record.getInt("BROAD_C") + record.getInt("O_BROAD_C")); // 스튜디오 방송 건수
		 				nightybroadstudioCell.setCellStyle(dataStyle);
		 				nightBroadys = nightBroadys + record.getInt("BROAD_C") + record.getInt("O_BROAD_C");
		 			   
		 			   
		 			  HSSFCell nightybroadcasterCell = nightybroadcaster.createCell(j-6);
		 			 nightybroadcasterCell.setCellValue(record.getInt("BROAD_P") + record.getInt("O_BROAD_P")); // 방송 건수 캐스터
		 			nightybroadcasterCell.setCellStyle(dataStyle);
		 			nightBroadyc = nightBroadyc + record.getInt("BROAD_P") + record.getInt("O_BROAD_P");
		 			  
		 			 HSSFCell nightnbroadcntCell = nightnbroadcnt.createCell(j-6);
		 			nightnbroadcntCell.setCellValue(record.getInt("BROAD_N") + record.getInt("O_BROAD_N")); // 비방송 건수
		 			nightnbroadcntCell.setCellStyle(dataStyle);
		 			nightBrodnsum = nightBrodnsum + record.getInt("BROAD_N") + record.getInt("O_BROAD_N");
		 			   
		 			   
		 			  HSSFCell nightnbroadperCell = nightnbroadper.createCell(j-6);
		 			 double hap4 = ((record.getInt("BROAD_N") + record.getInt("O_BROAD_N")) / ((double) record.getInt("SEND_Y") + (double) record.getInt("O_SEND_Y")) )* 100;
		 				hap4 = Math.round(hap4 * 10.0) / 10.0;
		 				nightnbroadperCell.setCellValue(hap4 + "%");
		 				nightnbroadperCell.setCellStyle(dataStyle);
		 				nightBrodnper = nightBrodnper + hap4;
		 			   
		 				HSSFCell nighticntCell = nighticnt.createCell(j-6);
		 				nighticntCell.setCellValue(record.getInt("IMP_Y") + record.getInt("O_IMP_Y")); // 전송 건수
		 				nighticntCell.setCellStyle(dataStyle);
		 				nightisum = nightisum + record.getInt("IMP_Y") + record.getInt("O_IMP_Y");

		 		 	  
		 		 	HSSFCell nightiybcntCell = nightiybcnt.createCell(j-6);
		 		 	nightiybcntCell.setCellValue(record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y")); // 주요제보 방송건수
		 		 	nightiybcntCell.setCellStyle(dataStyle);
		 		 	nightiybsum = nightiybsum +record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y");
			 		 	
			 		 	
			 		 	HSSFCell nightiybroadperCell = nightiybroadper.createCell(j-6);
			 		 	double hap5 = ((record.getInt("IMPBRO_Y") + record.getInt("O_IMPBRO_Y")) / ((double) record.getInt("IMP_Y") + (double) record.getInt("O_IMP_Y")) )* 100;
		 				hap5 = Math.round(hap5 * 10.0) / 10.0;
		 				nightiybroadperCell.setCellValue(hap5 + "%");
		 				nightiybroadperCell.setCellStyle(dataStyle);
		 				nightiybper = nightiybper + hap5;
		 				
		 				
		 				HSSFCell nightistudiorCell =nightistudio.createCell(j-6);
		 				nightistudiorCell.setCellValue(record.getInt("IMPBRO_P") + record.getInt("O_IMPBRO_P")); // 주요제보 스튜디오
		 				nightistudiorCell.setCellStyle(dataStyle);
		 				nightissum = nightissum +record.getInt("IMPBRO_P") + record.getInt("O_IMPBRO_P");
			 		 			
			 		 	HSSFCell nighticasterCell = nighticaster.createCell(j-6);
			 		 	nighticasterCell.setCellValue(record.getInt("IMPBRO_C") + record.getInt("O_IMPBRO_C"));// 주요제보 캐스터
			 		 	nighticasterCell.setCellStyle(dataStyle);
			 		 	nighticsum = nighticsum +record.getInt("IMPBRO_C") + record.getInt("O_IMPBRO_C");
		 				
		 			}
		 		}
	 		}
	 		sheet.setColumnWidth(1, 4000);
	 		sheet.setColumnWidth(2, 5000);
	 		sheet.setColumnWidth(3, 3500);


	 	}
	 	
	 	
	 	// 합계들 정산 및 엑셀에 넣기
	 	allSum = dayAllsum + nightAllsum; // 오전 오후 총 건수
	 	
	 	HSSFCell dayallrowCell = dayallrow.createCell(3);
	 	dayallrowCell.setCellValue(allSum); // 전체 건수 넣기
	 	dayallrowCell.setCellStyle(dataStyle);

	 	HSSFCell dayallrowCell2 = dayallrow.createCell(4);
	 	dayallrowCell2.setCellValue(dayAllsum); // 전체 건수 넣기
	 	dayallrowCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightallrowCell = nightallrow.createCell(4);
	 	nightallrowCell.setCellValue(nightAllsum); // 전체 건수 넣기
	 	nightallrowCell.setCellStyle(dataStyle);
	 	
	 	
	 	int allSendsum = daySendSum + nightSendSum;// 오전 오후 자체처리 총 건수
	 	HSSFCell daysendcntCell = daysendcnt.createCell(3);
	 	daysendcntCell.setCellValue(allSendsum); // 전체 건수 넣기
	 	daysendcntCell.setCellStyle(dataStyle);

	 	HSSFCell daysendcntCell2 = daysendcnt.createCell(4);
	 	daysendcntCell2.setCellValue(daySendSum); // 전체 건수 넣기
	 	daysendcntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightsendcntCell = nightsendcnt.createCell(4);
	 	nightsendcntCell.setCellValue(nightSendSum); // 전체 건수 넣기
	 	nightsendcntCell.setCellStyle(dataStyle);
	 	
	 	
		double allsendper = ((double) allSendsum/allSum)*100;
		double daysendperval = ((double) daySendSum/dayAllsum)*100;
		double nightsendperval = ((double) nightSendSum/nightAllsum)*100;
		
		allsendper = Math.round(allsendper * 10.0) / 10.0;
		daysendperval = Math.round(daysendperval * 10.0) / 10.0;
		nightsendperval = Math.round(nightsendperval * 10.0) / 10.0;
	 	
	 	HSSFCell daysendperCell = daysendper.createCell(3);
	 	daysendperCell.setCellValue(allsendper+ "%"); // 전체 건수 넣기
	 	daysendperCell.setCellStyle(dataStyle);

	 	HSSFCell daysendperCell2 = daysendper.createCell(4);
	 	daysendperCell2.setCellValue(daysendperval+ "%"); // 전체 건수 넣기
	 	daysendperCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightsendperCell = nightsendper.createCell(4);
	 	nightsendperCell.setCellValue(nightsendperval+ "%");// 전체 건수 넣기
	 	nightsendperCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	
	 	
	 	int allBrodsum = dayBroadsum + nightBroadSum;
	 	daybrodcnt.createCell(3).setCellValue(allBrodsum);
	 	daybrodcnt.createCell(4).setCellValue(dayBroadsum);
	 	nightbrodcnt.createCell(4).setCellValue(nightBroadSum);
	 	
	 	HSSFCell daybrodcntCell = daybrodcnt.createCell(3);
	 	daybrodcntCell.setCellValue(allBrodsum); // 전체 건수 넣기
	 	daybrodcntCell.setCellStyle(dataStyle);

	 	HSSFCell daybrodcntCell2 = daybrodcnt.createCell(4);
	 	daybrodcntCell2.setCellValue(dayBroadsum); // 전체 건수 넣기
	 	daybrodcntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightbrodcntCell = nightbrodcnt.createCell(4);
	 	nightbrodcntCell.setCellValue(nightBroadSum);// 전체 건수 넣기
	 	nightbrodcntCell.setCellStyle(dataStyle);
	 	
	 	double allbrodperval = ((double) allBrodsum/allSum)*100;
	 	double daybrodperval = ((double) dayBroadsum/dayAllsum)*100;
	 	double nightbrodperval = ((double) nightBroadSum/nightAllsum)*100;
	 	allbrodperval = Math.round(allbrodperval * 10.0) / 10.0;
	 	daybrodperval = Math.round(daybrodperval * 10.0) / 10.0;
	 	nightbrodperval = Math.round(nightbrodperval * 10.0) / 10.0;

	 	HSSFCell daybrodperCell = daybrodper.createCell(3);
	 	daybrodperCell.setCellValue(allbrodperval+ "%"); // 전체 건수 넣기
	 	daybrodperCell.setCellStyle(dataStyle);

	 	HSSFCell daybrodperCell2 = daybrodper.createCell(4);
	 	daybrodperCell2.setCellValue(daybrodperval+ "%"); // 전체 건수 넣기
	 	daybrodperCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightbrodperCell = nightbrodper.createCell(4);
	 	nightbrodperCell.setCellValue(nightbrodperval+ "%");// 전체 건수 넣기
	 	nightbrodperCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	int allBrodYsum = dayBroadysum + nightBroadysum;
	 	
	 	HSSFCell dayybroadcntCell = dayybroadcnt.createCell(3);
	 	dayybroadcntCell.setCellValue(allBrodYsum); // 전체 건수 넣기
	 	dayybroadcntCell.setCellStyle(dataStyle);

	 	HSSFCell dayybroadcntCell2 = dayybroadcnt.createCell(4);
	 	dayybroadcntCell2.setCellValue(dayBroadysum); // 전체 건수 넣기
	 	dayybroadcntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightybroadcntCell = nightybroadcnt.createCell(4);
	 	nightybroadcntCell.setCellValue(nightBroadysum);// 전체 건수 넣기
	 	nightybroadcntCell.setCellStyle(dataStyle);
	 		 	
	 	
	 	double allbrodyperval = ((double) allBrodYsum/allBrodsum)*100;
	 	double daybrodyperval = ((double) dayBroadysum/dayBroadsum)*100;
	 	double nightbrodyperval = ((double) nightBroadysum/nightBroadSum)*100;
	 	allbrodyperval = Math.round(allbrodyperval * 10.0) / 10.0;
	 	daybrodyperval = Math.round(daybrodyperval * 10.0) / 10.0;
	 	nightbrodyperval = Math.round(nightbrodyperval * 10.0) / 10.0;
	 	HSSFCell dayybroadperCell = dayybroadper.createCell(3);
	 	dayybroadperCell.setCellValue(allbrodyperval+ "%"); // 전체 건수 넣기
	 	dayybroadperCell.setCellStyle(dataStyle);

	 	HSSFCell dayybroadperCell2 = dayybroadper.createCell(4);
	 	dayybroadperCell2.setCellValue(daybrodyperval+ "%"); // 전체 건수 넣기
	 	dayybroadperCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightybroadperCell = nightybroadper.createCell(4);
	 	nightybroadperCell.setCellValue(nightbrodyperval+ "%");// 전체 건수 넣기
	 	nightybroadperCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	
	 	int allBroadYstudio = dayBroadys + nightBroadys;
	 	
	 	HSSFCell dayybroadstudioCell = dayybroadstudio.createCell(3);
	 	dayybroadstudioCell.setCellValue(allBroadYstudio); // 전체 건수 넣기
	 	dayybroadstudioCell.setCellStyle(dataStyle);

	 	HSSFCell dayybroadstudioCell2 = dayybroadstudio.createCell(4);
	 	dayybroadstudioCell2.setCellValue(dayBroadys); // 전체 건수 넣기
	 	dayybroadstudioCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightybroadstudioCell = nightybroadstudio.createCell(4);
	 	nightybroadstudioCell.setCellValue(nightBroadys);// 전체 건수 넣기
	 	nightybroadstudioCell.setCellStyle(dataStyle);
	 	
	 	int allBroadCstudio = dayBroadyc + nightBroadyc;
	 	
	 	HSSFCell dayybroadcasterCell = dayybroadcaster.createCell(3);
	 	dayybroadcasterCell.setCellValue(allBroadCstudio); // 전체 건수 넣기
	 	dayybroadcasterCell.setCellStyle(dataStyle);

	 	HSSFCell dayybroadcasterCell2 = dayybroadcaster.createCell(4);
	 	dayybroadcasterCell2.setCellValue(dayBroadyc); // 전체 건수 넣기
	 	dayybroadcasterCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightybroadcasterCell = nightybroadcaster.createCell(4);
	 	nightybroadcasterCell.setCellValue(nightBroadyc);// 전체 건수 넣기
	 	nightybroadcasterCell.setCellStyle(dataStyle);
	 		 	
	 	
	 	
	 	int allBroadNo = dayBrodnsum + nightBrodnsum;
	 	
	 	HSSFCell daynbroadcntCell = daynbroadcnt.createCell(3);
	 	daynbroadcntCell.setCellValue(allBroadNo); // 전체 건수 넣기
	 	daynbroadcntCell.setCellStyle(dataStyle);

	 	HSSFCell daynbroadcntCell2 = daynbroadcnt.createCell(4);
	 	daynbroadcntCell2.setCellValue(dayBrodnsum); // 전체 건수 넣기
	 	daynbroadcntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightnbroadcntCell = nightnbroadcnt.createCell(4);
	 	nightnbroadcntCell.setCellValue(nightBrodnsum);// 전체 건수 넣기
	 	nightnbroadcntCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	double allbrodnperval = ((double) allBroadNo/allBrodsum)*100;
	 	double daybrodnperval = ((double) dayBrodnsum/dayBroadsum)*100;
	 	double nightbrodnperval = ((double) nightBrodnsum/nightBroadSum)*100;
	 	allbrodnperval = Math.round(allbrodnperval * 10.0) / 10.0;
	 	daybrodnperval = Math.round(daybrodnperval * 10.0) / 10.0;
	 	nightbrodnperval = Math.round(nightbrodnperval * 10.0) / 10.0;
	 	
	 	HSSFCell daynbroadperCell = daynbroadper.createCell(3);
	 	daynbroadperCell.setCellValue(allbrodnperval+ "%"); // 전체 건수 넣기
	 	daynbroadperCell.setCellStyle(dataStyle);

	 	HSSFCell daynbroadperCell2 = daynbroadper.createCell(4);
	 	daynbroadperCell2.setCellValue(daybrodnperval+ "%"); // 전체 건수 넣기
	 	daynbroadperCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightnbroadperCell = nightnbroadper.createCell(4);
	 	nightnbroadperCell.setCellValue(nightbrodnperval+ "%");// 전체 건수 넣기
	 	nightnbroadperCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	int allImpSend = dayisum + nightisum;  // 전송 건수
	 	
	 	
	 	HSSFCell dayicntCell = dayicnt.createCell(3);
	 	dayicntCell.setCellValue(allImpSend); // 전체 건수 넣기
	 	dayicntCell.setCellStyle(dataStyle);

	 	HSSFCell dayicntCell2 = dayicnt.createCell(4);
	 	dayicntCell2.setCellValue(dayisum); // 전체 건수 넣기
	 	dayicntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nighticntCell = nighticnt.createCell(4);
	 	nighticntCell.setCellValue(nightisum);// 전체 건수 넣기
	 	nighticntCell.setCellStyle(dataStyle);
	 	
	 	
	 	int allImpBrod = dayiybsum + nightiybsum; // 방송 건수

	 	HSSFCell dayiybcntCell = dayiybcnt.createCell(3);
	 	dayiybcntCell.setCellValue(allImpBrod); // 전체 건수 넣기
	 	dayiybcntCell.setCellStyle(dataStyle);

	 	HSSFCell dayiybcntCell2 = dayiybcnt.createCell(4);
	 	dayiybcntCell2.setCellValue(dayiybsum); // 전체 건수 넣기
	 	dayiybcntCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightiybcntCell = nightiybcnt.createCell(4);
	 	nightiybcntCell.setCellValue(nightiybsum);// 전체 건수 넣기
	 	nightiybcntCell.setCellStyle(dataStyle);
	 	
	 	
	 	
	 	
	 	double allimbrodperval = ((double) allImpBrod/allImpSend)*100;
	 	double dayimbrodperval = ((double) dayiybsum/dayisum)*100;
	 	double nightimbrodperval = ((double) nightiybsum/nightisum)*100;
	 	allimbrodperval = Math.round(allimbrodperval * 10.0) / 10.0;
	 	dayimbrodperval = Math.round(dayimbrodperval * 10.0) / 10.0;
	 	nightimbrodperval = Math.round(nightimbrodperval * 10.0) / 10.0;
	 	
	 	HSSFCell dayiybroadperCell = dayiybroadper.createCell(3);
	 	dayiybroadperCell.setCellValue(allimbrodperval+ "%"); // 전체 건수 넣기
	 	dayiybroadperCell.setCellStyle(dataStyle);

	 	HSSFCell dayiybroadperCell2 = dayiybroadper.createCell(4);
	 	dayiybroadperCell2.setCellValue(dayimbrodperval+ "%"); // 전체 건수 넣기
	 	dayiybroadperCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightiybroadperCell = nightiybroadper.createCell(4);
	 	nightiybroadperCell.setCellValue(nightimbrodperval+ "%");// 전체 건수 넣기
	 	nightiybroadperCell.setCellStyle(dataStyle);
	 	
	 	int allImpStudio = dayissum + nightissum;

	 	HSSFCell dayistudioCell = dayistudio.createCell(3);
	 	dayistudioCell.setCellValue(allImpStudio); // 전체 건수 넣기
	 	dayistudioCell.setCellStyle(dataStyle);

	 	HSSFCell dayistudioCell2 = dayistudio.createCell(4);
	 	dayistudioCell2.setCellValue(dayissum); // 전체 건수 넣기
	 	dayistudioCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nightistudioCell = nightistudio.createCell(4);
	 	nightistudioCell.setCellValue(nightissum);// 전체 건수 넣기
	 	nightistudioCell.setCellStyle(dataStyle);
	 	
	 	int allImpCaster = dayicsum + nighticsum;
	 	
	 	HSSFCell dayicasterCell = dayicaster.createCell(3);
	 	dayicasterCell.setCellValue(allImpCaster); // 전체 건수 넣기
	 	dayicasterCell.setCellStyle(dataStyle);

	 	HSSFCell dayicasterCell2 = dayicaster.createCell(4);
	 	dayicasterCell2.setCellValue(dayicsum); // 전체 건수 넣기
	 	dayicasterCell2.setCellStyle(dataStyle);
	 	
	 	HSSFCell nighticasterCell = nighticaster.createCell(4);
	 	nighticasterCell.setCellValue(nighticsum);// 전체 건수 넣기
	 	nighticasterCell.setCellStyle(dataStyle);
	 	
 	    
 	    
    	// 기간(일) 구하기
 	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
 	   
 	    LocalDate start = LocalDate.parse(startDate, formatter); //date 형으로 변환
 	    LocalDate end = LocalDate.parse(endDate, formatter);
 	    
 	    long daysBetween = ChronoUnit.DAYS.between(start, end); // 두 날짜 사의 값 구하기
 	    int daysCntval = (int) daysBetween + 1; // 명시적 변환 => 연산을 위해 int 형으로 형 변환
 	    int dayCnt = 0;
 	    
 	    if(daysCntval < 1) {
 	    	dayCnt = allSum;
 	    } else {
 	    	dayCnt = allSum / daysCntval;
 	    }
 	    
 	    
	    CellStyle daycntStyle = wb.createCellStyle();
	    daycntStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 오른쪽 정렬 (가로 기준)
	    daycntStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
	    
	    Font daycntfont = wb.createFont(); // 폰트 객체 생성
	    daycntfont.setFontHeightInPoints((short) 14); // 폰트 크기 설정   
	    daycntfont.setFontName("굴림체");
	    daycntStyle.setFont(daycntfont); // 폰트 스타일을 셀 스타일에 적용
	    
        // 일당 교통정보 현황 건수 row (A4) => 총 접수 건수 / 기간
        HSSFRow daycntrow = sheet.createRow(3);
        HSSFCell daycntcell = daycntrow.createCell(0);
        
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3)); // title cell 병합 / 예제 파일 기준으로 5칸 병합
        daycntcell.setCellValue(" ■ 교통정보 현황: " + dayCnt + "건 / 일");
        daycntcell.setCellStyle(daycntStyle); // 스타일 적용
		
        // cell width ,  height 변경
 	    sheet.setColumnWidth(0, 3700); // setColumnWidth(변경 할 cell , 크기)
 	    daycntrow.setHeightInPoints(25.88f); //setHeightInPoints 크기)

    }
    
    // 연간 제보자별 제보현황
    public void yearReceipt(Map model, HSSFWorkbook wb) {
    	
    	List dataList = (List) model.get("dataList"); // 엑셀에서 사용할 데이터 가져오기
    	List<AwardVO> perList = (List<AwardVO>)model.get("perList");
    	List totalList = (List) model.get("totalList");
    	
    	
    	Map<String, Object> total = (Map<String, Object>) totalList.get(0);
    	AwardVO per = (AwardVO) perList.get(0);
    	
    	int allSum = 0; // 총 건수 => 엑셀 상단에 사용
    	int allInformer = dataList.size(); // 총 인원 수 => 엑셀 상단에 사용
    	
    	int allPer = Integer.parseInt(per.getALL_PER()); // 총점 기준 %
    	int maxSend = Integer.parseInt(total.get("TOTAL").toString()); // 최대 제보자 건수
    	
    	
    	int sendNsum = 0; // 제보건수
    	int sendYsum = 0; // 방송 건수
    	int importentSum = 0; // 주요 제보
    	
    	// sheet 생성 및 이름 저장
    	HSSFSheet sheet = wb.createSheet("연간 제보자별 제보현황");
    	
    	
    	// title style 변경 
        // style 객체 생성
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 40); // 폰트 크기 설정
 	    font.setFontName("굴림체");
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
    	
 	    // 엑셀 title 넣을 행 생성 (A1)
    	HSSFRow titlerow = sheet.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0); // 셀 생성
    	
        // CellRangeAddress(a,b,c,d) 사용법
        // => CellRangeAddress(첫 행,마지막 행,첫 열,마지막 열)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 28)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
        
        titleCell.setCellValue("제보자별 제보현황 (연간)"); // title row에 title 삽입
        titleCell.setCellStyle(titleStyle); // 스타일 적용

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font datefont = wb.createFont(); // 폰트 객체 생성
 	    datefont.setFontHeightInPoints((short) 18); // 폰트 크기 설정
 	    datefont.setFontName("굴림체");
 	    dateStyle.setFont(datefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
        // 기간 행 생성
        HSSFRow daterow = sheet.createRow(2);
        HSSFCell dateCell = daterow.createCell(0); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 28)); // title cell 병합 / 예제 파일 기준으로 17칸 병합  		

     	// 통계 제목 밑 일자 변환
     	String getStartDate = (String) model.get("start_date");
     	String startY = getStartDate.substring(0,4) + "년 ";
     	String startM = getStartDate.substring(4,6) + "월 ";
     	String startD = getStartDate.substring(6,8) + "일 ~ ";
     				
     	String startDate = startY + startM + startD;
     				
     	String getEndDate = (String) model.get("end_date");
     	String endY = getEndDate.substring(0,4) + "년 ";
     	String endM = getEndDate.substring(4,6) + "월 ";
     	String endD = getEndDate.substring(6,8) + "일";
     				
     	String endDate = endY + endM + endD;
     	
     	dateCell.setCellValue(startDate + endDate);
     	dateCell.setCellStyle(dateStyle); // 스타일 적용
     	
     	
     	CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용 	
     	
		
		
		
		CellStyle sendStyle = wb.createCellStyle();
		sendStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	sendStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	sendStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	sendStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	sendStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	sendStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	sendStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	sendStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font sendStyleF = wb.createFont(); // 폰트 객체 생성
		sendStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		sendStyleF.setFontName("굴림체");
		sendStyle.setFont(sendStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		
		
		CellStyle scoreStyle = wb.createCellStyle();
		scoreStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
		scoreStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		scoreStyle.setFillForegroundColor(IndexedColors.TAN.getIndex()); // 배경색 설정*/ 	   
		scoreStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		scoreStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		scoreStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		scoreStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		scoreStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font scoreStyleF = wb.createFont(); // 폰트 객체 생성
		scoreStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		scoreStyleF.setFontName("굴림체");
		scoreStyle.setFont(scoreStyleF); // 폰트 스타일을 셀 스타일에 적용 
     	
     	
     	
     	
     	HSSFRow headrow = sheet.createRow(6);
     	HSSFRow headrow2 = sheet.createRow(7);
     	
     	HSSFCell idCell = headrow.createCell(0); // ID
     	HSSFCell nameCell = headrow.createCell(1); // 이름
     	HSSFCell orgCell = headrow.createCell(2); // 소속
     	HSSFCell callCell = headrow.createCell(3); // 연락처
     	HSSFCell sendCell = headrow.createCell(4); // 제보건수
     	
     	HSSFCell idCell2 = headrow2.createCell(0); // ID
     	HSSFCell nameCell2 = headrow2.createCell(1); // 이름
     	HSSFCell orgCell2 = headrow2.createCell(2); // 소속
     	HSSFCell callCell2 = headrow2.createCell(3); // 연락처
     	HSSFCell sendCell2 = headrow2.createCell(4); // 제보건수
     	
     	sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 0)); 
     	sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 1)); 
     	sheet.addMergedRegion(new CellRangeAddress(6, 7, 2, 2)); 
     	sheet.addMergedRegion(new CellRangeAddress(6, 7, 3, 3)); 
        sheet.addMergedRegion(new CellRangeAddress(6, 7, 4, 4)); 
     	
        idCell.setCellValue("ID"); 
        nameCell.setCellValue("이름"); 
        orgCell.setCellValue("소속"); 
        callCell.setCellValue("연락처"); 
        sendCell.setCellValue("제보건수");

        idCell.setCellStyle(headStyle); // 스타일 적용
        nameCell.setCellStyle(headStyle); // 스타일 적용
        orgCell.setCellStyle(headStyle); // 스타일 적용
        callCell.setCellStyle(headStyle); // 스타일 적용
        sendCell.setCellStyle(sendStyle); // 스타일 적용
        
        idCell2.setCellStyle(headStyle); // 스타일 적용
        nameCell2.setCellStyle(headStyle); // 스타일 적용
        orgCell2.setCellStyle(headStyle); // 스타일 적용
        callCell2.setCellStyle(headStyle); // 스타일 적용
        sendCell2.setCellStyle(sendStyle); // 스타일 적용
        
        
        for(int i = 0; i < 12; i++ ) {
        	HSSFCell monthCell = headrow.createCell(5 + (i * 2));
        	HSSFCell monthCell2 = headrow.createCell(6 + (i * 2)); 
         	HSSFCell sendminCell = headrow2.createCell(5 + (i * 2)); 
         	HSSFCell scoreCell = headrow2.createCell(6 + (i * 2)); 
         	
         	sheet.addMergedRegion(new CellRangeAddress(6, 6, 5 + (i * 2), 6 + (i * 2))); 
         	
         	monthCell.setCellValue((i+1) + "월"); 
         	sendminCell.setCellValue("제보건수"); 
         	scoreCell.setCellValue("총점"); 
         	
         	monthCell.setCellStyle(headStyle); // 스타일 적용
         	monthCell2.setCellStyle(headStyle);
         	sendminCell.setCellStyle(sendStyle); // 스타일 적용
         	scoreCell.setCellStyle(scoreStyle); // 스타일 적용
        }
        
        int allSendCnt = 0; // 총 건수
        int allSend = 0; // 제보 건수
        int rowcnt = 8; //행 카운터
        
        DataFormat format = wb.createDataFormat();

        
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setDataFormat(format.getFormat("#,##0"));
		
		Font dataStyleF = wb.createFont(); // 폰트 객체 생성
		dataStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyleF.setFontName("굴림체");
		dataStyle.setFont(dataStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		
		CellStyle dataStyle2 = wb.createCellStyle();
        dataStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
        dataStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setDataFormat(format.getFormat("#,##0"));
		
		Font dataStyle2F = wb.createFont(); // 폰트 객체 생성
		dataStyle2F.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyle2F.setFontName("굴림체");
		dataStyle2.setFont(dataStyle2F); // 폰트 스타일을 셀 스타일에 적용 
		
		CellStyle dataStyle3 = wb.createCellStyle();
        dataStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
        dataStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font dataStyle3F = wb.createFont(); // 폰트 객체 생성
		dataStyle3F.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyle3F.setFontName("굴림체");
		dataStyle3.setFont(dataStyle3F); // 폰트 스타일을 셀 스타일에 적용 

		int Mval = 0;
		
        for(int i = 0; i < allInformer; i++) {
        	int infrmSend = 0; // 통신원 개인 별 제보 건수 카운터
        	Map<String, Object> record = (Map<String, Object>) dataList.get(i);
        	
        	HSSFRow datarow = sheet.createRow(rowcnt); // 1. 행 생성
        	
        	HSSFCell id = datarow.createCell(0);
        	id.setCellValue(record.get("ACT_ID").toString()); 
        	id.setCellStyle(dataStyle);
        	
        	HSSFCell name = datarow.createCell(1);
        	name.setCellValue(record.get("INFORMER_NAME").toString()); 
        	name.setCellStyle(dataStyle);
        	
        	HSSFCell org = datarow.createCell(2);
        	org.setCellValue(record.get("ORG_NAME").toString()); 
        	org.setCellStyle(dataStyle);
        	
        	HSSFCell call = datarow.createCell(3);
        	call.setCellValue(record.get("PHONE_CELL").toString()); 
        	call.setCellStyle(dataStyle);
        	
        	// 월별 데이터 넣기
        	HSSFCell JAN = datarow.createCell(5); //1월
        	HSSFCell JANval = datarow.createCell(6);
        	
        	Mval = Integer.parseInt(record.get("JAN").toString());
        	JAN.setCellValue(Integer.parseInt(record.get("JAN").toString()));
        	JANval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);

        	JAN.setCellStyle(dataStyle2);
        	JANval.setCellStyle(dataStyle3);
        	
        	infrmSend = infrmSend + Integer.parseInt(record.get("JAN").toString());
        	
        	
        	
        	HSSFCell FEB = datarow.createCell(7); //2월
        	HSSFCell FEBval = datarow.createCell(8);
        	
        	Mval = Integer.parseInt(record.get("FEB").toString());
        	FEB.setCellValue(Integer.parseInt(record.get("FEB").toString()));
        	FEBval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	FEB.setCellStyle(dataStyle2);
        	FEBval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("FEB").toString());
        	
        	
        	
        	HSSFCell MAR = datarow.createCell(9); //3월
        	HSSFCell MARval = datarow.createCell(10);
        	
        	Mval = Integer.parseInt(record.get("MAR").toString());
        	MAR.setCellValue(Integer.parseInt(record.get("MAR").toString()));
        	MARval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	MAR.setCellStyle(dataStyle2);
        	MARval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("MAR").toString());
        	
        	HSSFCell APR = datarow.createCell(11); //4월
        	HSSFCell APRval = datarow.createCell(12);
        	
        	Mval = Integer.parseInt(record.get("APR").toString());
        	APR.setCellValue(Integer.parseInt(record.get("APR").toString()));
        	APRval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	APR.setCellStyle(dataStyle2);
        	APRval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend +Integer.parseInt(record.get("APR").toString());
        	
        	
        	
        	
        	HSSFCell MAY = datarow.createCell(13); //5월
        	HSSFCell MAYval = datarow.createCell(14);
        	
        	Mval = Integer.parseInt(record.get("MAY").toString());
        	MAY.setCellValue(Integer.parseInt(record.get("MAY").toString()));
        	MAYval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	MAY.setCellStyle(dataStyle2);
        	MAYval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend +Integer.parseInt(record.get("MAY").toString());
        	
        	
        	
        	
        	HSSFCell JUN = datarow.createCell(15);//6월
        	HSSFCell JUNval = datarow.createCell(16);
        	
        	Mval = Integer.parseInt(record.get("JUN").toString());
        	JUN.setCellValue(Integer.parseInt(record.get("JUN").toString()));
        	JUNval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	JUN.setCellStyle(dataStyle2);
        	JUNval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("JUN").toString());
        	
        	
        	
        	
        	HSSFCell JUL = datarow.createCell(17); //7월
        	HSSFCell JULval = datarow.createCell(18);
        	
        	Mval = Integer.parseInt(record.get("JUL").toString());
        	JUL.setCellValue(Integer.parseInt(record.get("JUL").toString()));
        	JULval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	JUL.setCellStyle(dataStyle2);
        	JULval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("JUL").toString());
        	
        	
        	
        	
        	HSSFCell AUG = datarow.createCell(19); //8월
        	HSSFCell AUGval = datarow.createCell(20);
        	
        	Mval = Integer.parseInt(record.get("AUG").toString());
        	AUG.setCellValue(Integer.parseInt(record.get("AUG").toString()));
        	AUGval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	AUG.setCellStyle(dataStyle2);
        	AUGval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("AUG").toString());
        	
        	
        	
        	
        	HSSFCell SEP = datarow.createCell(21); //9월
        	HSSFCell SEPval = datarow.createCell(22);
        	
        	Mval = Integer.parseInt(record.get("SEP").toString());
        	SEP.setCellValue(Integer.parseInt(record.get("SEP").toString()));
        	SEPval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	SEP.setCellStyle(dataStyle2);
        	SEPval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend +Integer.parseInt(record.get("SEP").toString());
        	
        	
        	
        	
        	HSSFCell OCT = datarow.createCell(23); //10월
        	HSSFCell OCTval = datarow.createCell(24);
        	
        	Mval = Integer.parseInt(record.get("OCT").toString());
        	OCT.setCellValue(Integer.parseInt(record.get("OCT").toString()));
        	OCTval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	OCT.setCellStyle(dataStyle2);
        	OCTval.setCellStyle(dataStyle3); 
        	infrmSend = infrmSend + Integer.parseInt(record.get("OCT").toString());
        	
        	
        	
        	
        	HSSFCell NOV = datarow.createCell(25); //11월
        	HSSFCell NOVval = datarow.createCell(26);
        	
        	Mval = Integer.parseInt(record.get("NOV").toString());
        	NOV.setCellValue(Integer.parseInt(record.get("NOV").toString()));
        	NOVval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	NOV.setCellStyle(dataStyle2);
        	NOVval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("NOV").toString());
        	
        	
        	
        	
        	HSSFCell DEC = datarow.createCell(27); //12월
        	HSSFCell DECval = datarow.createCell(28);
        	
        	Mval = Integer.parseInt(record.get("DEC").toString());
        	DEC.setCellValue(Integer.parseInt(record.get("DEC").toString()));
        	DECval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	DEC.setCellStyle(dataStyle2);
        	DECval.setCellStyle(dataStyle3);
        	infrmSend = infrmSend + Integer.parseInt(record.get("DEC").toString());
        	
        	HSSFCell allsendCell = datarow.createCell(4); // 제보건수 넣기
        	allsendCell.setCellValue(infrmSend);
        	allsendCell.setCellStyle(dataStyle);
        	allSend = allSend + infrmSend; // 총 제보건수에 추가
        	rowcnt++; // 행 카운터 증가
        	Mval = 0;
        }
     	
        
        CellStyle infoStyle = wb.createCellStyle();
        infoStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		
		Font infoStyleF = wb.createFont(); // 폰트 객체 생성
		infoStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		infoStyleF.setFontName("굴림체");
		infoStyle.setFont(infoStyleF); // 폰트 스타일을 셀 스타일에 적용 
        
        
        // 총 인원수, 총 건수 넣기
        HSSFRow inforow = sheet.createRow(5);
        
        HSSFCell allInfrmCell = inforow.createCell(0); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2)); 
        allInfrmCell.setCellValue("총 인원 : " + allInformer + "명");
        allInfrmCell.setCellStyle(infoStyle);
        
        HSSFCell allsendCell = inforow.createCell(5); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 10)); 
        allsendCell.setCellValue("총 건수 : " + allSend + "건");
        allsendCell.setCellStyle(infoStyle);
        
        sheet.setColumnWidth(3, 6000);
    }
    
    
    
    // 연간 지역소속별 통계
    public void yearOrgStat(Map model, HSSFWorkbook wb) {
    	List dataList = (List) model.get("dataList"); // 엑셀에서 사용할 데이터 가져오기
    	List<AwardVO> perList = (List<AwardVO>)model.get("perList");
    	List totalList = (List) model.get("totalList");
    	
    	
    	Map<String, Object> total = (Map<String, Object>) totalList.get(0);
    	AwardVO per = (AwardVO) perList.get(0);
    	
    	int allSum = 0; // 총 건수 => 엑셀 상단에 사용
    	int allInformer = dataList.size(); // 총 인원 수 => 엑셀 상단에 사용
    	
    	int allPer = Integer.parseInt(per.getALL_PER()); // 총점 기준 %
    	int maxSend = Integer.parseInt(total.get("TOTAL").toString()); // 최대 제보자 건수

    	
    	// sheet 생성 및 이름 저장
    	HSSFSheet sheet = wb.createSheet("연간 제보자별 제보현황");
    	
    	
    	// title style 변경 
        // style 객체 생성
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 40); // 폰트 크기 설정
 	    font.setFontName("굴림체");
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
    	
 	    // 엑셀 title 넣을 행 생성 (A1)
    	HSSFRow titlerow = sheet.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0); // 셀 생성
    	
        // CellRangeAddress(a,b,c,d) 사용법
        // => CellRangeAddress(첫 행,마지막 행,첫 열,마지막 열)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 25)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
        
        titleCell.setCellValue("지회별 실적평가 (연간)"); // title row에 title 삽입
        titleCell.setCellStyle(titleStyle); // 스타일 적용

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        dateStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font datefont = wb.createFont(); // 폰트 객체 생성
 	    datefont.setFontHeightInPoints((short) 18); // 폰트 크기 설정
 	    datefont.setFontName("굴림체");
 	    dateStyle.setFont(datefont); // 폰트 스타일을 셀 스타일에 적용
 	    
 	    
        // 기간 행 생성
        HSSFRow daterow = sheet.createRow(2);
        HSSFCell dateCell = daterow.createCell(0); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 25)); // title cell 병합 / 예제 파일 기준으로 17칸 병합  		

     	// 통계 제목 밑 일자 변환
     	String getStartDate = (String) model.get("start_date");
     	String startY = getStartDate.substring(0,4) + "년 ";
     	String startM = getStartDate.substring(4,6) + "월 ";
     	String startD = getStartDate.substring(6,8) + "일 ~ ";
     				
     	String startDate = startY + startM + startD;
     				
     	String getEndDate = (String) model.get("end_date");
     	String endY = getEndDate.substring(0,4) + "년 ";
     	String endM = getEndDate.substring(4,6) + "월 ";
     	String endD = getEndDate.substring(6,8) + "일";
     				
     	String endDate = endY + endM + endD;
     	
     	dateCell.setCellValue(startDate + endDate);
     	dateCell.setCellStyle(dateStyle); // 스타일 적용
     	
     	CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용 	
     	
		
		
		
		CellStyle sendStyle = wb.createCellStyle();
		sendStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	sendStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	sendStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // 배경색 설정*/ 	   
     	sendStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	sendStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	sendStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	sendStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	sendStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font sendStyleF = wb.createFont(); // 폰트 객체 생성
		sendStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		sendStyleF.setFontName("굴림체");
		sendStyle.setFont(sendStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		
		
		CellStyle scoreStyle = wb.createCellStyle();
		scoreStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
		scoreStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		scoreStyle.setFillForegroundColor(IndexedColors.TAN.getIndex()); // 배경색 설정*/ 	   
		scoreStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
		scoreStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
		scoreStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		scoreStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		scoreStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font scoreStyleF = wb.createFont(); // 폰트 객체 생성
		scoreStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		scoreStyleF.setFontName("굴림체");
		scoreStyle.setFont(scoreStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);
		
		
		
     	HSSFRow headrow = sheet.createRow(6);
     	HSSFRow headrow2 = sheet.createRow(7);
     	
     	HSSFCell orgCell = headrow.createCell(0); // 소속
     	HSSFCell sendCell = headrow.createCell(1); // 제보건수
     	
     	HSSFCell orgCell2 = headrow2.createCell(0); // 소속
     	HSSFCell sendCell2 = headrow2.createCell(1); // 제보건수
     	
     	sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 0)); 
        sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 1)); 
     	
        orgCell.setCellValue("소속"); 
        sendCell.setCellValue("제보건수");

        orgCell.setCellStyle(headStyle); // 스타일 적용
        sendCell.setCellStyle(sendStyle); // 스타일 적용
        
        orgCell2.setCellStyle(headStyle); // 스타일 적용
        sendCell2.setCellStyle(sendStyle); // 스타일 적용
        
        
        for(int i = 0; i < 12; i++ ) {
        	HSSFCell monthCell = headrow.createCell(2 + (i * 2));
        	HSSFCell monthCell2 = headrow.createCell(3 + (i * 2)); 
         	HSSFCell sendminCell = headrow2.createCell(2 + (i * 2)); 
         	HSSFCell scoreCell = headrow2.createCell(3 + (i * 2)); 
         	
         	sheet.addMergedRegion(new CellRangeAddress(6, 6, 2 + (i * 2), 3 + (i * 2))); 
         	
         	monthCell.setCellValue((i+1) + "월"); 
         	sendminCell.setCellValue("제보건수"); 
         	scoreCell.setCellValue("총점"); 
         	
         	monthCell.setCellStyle(headStyle); // 스타일 적용
         	monthCell2.setCellStyle(headStyle);
         	sendminCell.setCellStyle(sendStyle); // 스타일 적용
         	scoreCell.setCellStyle(scoreStyle); // 스타일 적용
         	
         	sheet.setColumnWidth(2 + (i * 2), 3500);
         	sheet.setColumnWidth(3 + (i * 2), 3500);
        }
     
        DataFormat format = wb.createDataFormat();
        
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setDataFormat(format.getFormat("#,##0"));
		
		Font dataStyleF = wb.createFont(); // 폰트 객체 생성
		dataStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyleF.setFontName("굴림체");
		dataStyle.setFont(dataStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
		
		CellStyle dataStyle2 = wb.createCellStyle();
        dataStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
        dataStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle2.setDataFormat(format.getFormat("#,##0"));
		
		Font dataStyle2F = wb.createFont(); // 폰트 객체 생성
		dataStyle2F.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyle2F.setFontName("굴림체");
		dataStyle2.setFont(dataStyle2F); // 폰트 스타일을 셀 스타일에 적용 
		
		
		CellStyle dataStyle3 = wb.createCellStyle();
        dataStyle3.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
        dataStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle3.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle3.setDataFormat(format.getFormat("#,##0"));
		
		Font dataStyle3F = wb.createFont(); // 폰트 객체 생성
		dataStyle3F.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyle3F.setFontName("굴림체");
		dataStyle3.setFont(dataStyle3F); // 폰트 스타일을 셀 스타일에 적용 
		
		
        int rowcnt = 8; //행 카운터
        int Mval = 0;
        
        
        // 받아온 데이터 넣기
        for(int i=0; i < allInformer; i++) {
        	int monthSum = 0;
        	Map<String, Object> record = (Map<String, Object>) dataList.get(i);
        	
        	HSSFRow datarow = sheet.createRow(rowcnt); // 1. 행 생성
        	
        	HSSFCell org = datarow.createCell(0);
        	org.setCellValue(record.get("ORG_NAME").toString()); 
        	org.setCellStyle(dataStyle);
        	
        	// 월별 데이터 넣기
        	HSSFCell JAN = datarow.createCell(2); //1월
        	HSSFCell JANval = datarow.createCell(3);
        	
        	Mval = Integer.parseInt(record.get("JAN").toString());
        	JAN.setCellValue(Integer.parseInt(record.get("JAN").toString()));
        	JANval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);

        	JAN.setCellStyle(dataStyle2);
        	JANval.setCellStyle(dataStyle3);
        	
        	monthSum = monthSum + Integer.parseInt(record.get("JAN").toString());
        	
        	
        	
        	HSSFCell FEB = datarow.createCell(4); //2월
        	HSSFCell FEBval = datarow.createCell(5);
        	
        	Mval = Integer.parseInt(record.get("FEB").toString());
        	FEB.setCellValue(Integer.parseInt(record.get("FEB").toString()));
        	FEBval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	FEB.setCellStyle(dataStyle2);
        	FEBval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("FEB").toString());
        	
        	
        	
        	HSSFCell MAR = datarow.createCell(6); //3월
        	HSSFCell MARval = datarow.createCell(7);
        	
        	Mval = Integer.parseInt(record.get("MAR").toString());
        	MAR.setCellValue(Integer.parseInt(record.get("MAR").toString()));
        	MARval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	MAR.setCellStyle(dataStyle2);
        	MARval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("MAR").toString());
        	
        	HSSFCell APR = datarow.createCell(8); //4월
        	HSSFCell APRval = datarow.createCell(9);
        	
        	Mval = Integer.parseInt(record.get("APR").toString());
        	APR.setCellValue(Integer.parseInt(record.get("APR").toString()));
        	APRval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	APR.setCellStyle(dataStyle2);
        	APRval.setCellStyle(dataStyle3);
        	monthSum = monthSum +Integer.parseInt(record.get("APR").toString());
        	
        	
        	
        	
        	HSSFCell MAY = datarow.createCell(10); //5월
        	HSSFCell MAYval = datarow.createCell(11);
        	
        	Mval = Integer.parseInt(record.get("MAY").toString());
        	MAY.setCellValue(Integer.parseInt(record.get("MAY").toString()));
        	MAYval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	MAY.setCellStyle(dataStyle2);
        	MAYval.setCellStyle(dataStyle3);
        	monthSum = monthSum +Integer.parseInt(record.get("MAY").toString());
        	
        	
        	
        	
        	HSSFCell JUN = datarow.createCell(12);//6월
        	HSSFCell JUNval = datarow.createCell(13);
        	
        	Mval = Integer.parseInt(record.get("JUN").toString());
        	JUN.setCellValue(Integer.parseInt(record.get("JUN").toString()));
        	JUNval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	JUN.setCellStyle(dataStyle2);
        	JUNval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("JUN").toString());
        	
        	
        	
        	
        	HSSFCell JUL = datarow.createCell(14); //7월
        	HSSFCell JULval = datarow.createCell(15);
        	
        	Mval = Integer.parseInt(record.get("JUL").toString());
        	JUL.setCellValue(Integer.parseInt(record.get("JUL").toString()));
        	JULval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	JUL.setCellStyle(dataStyle2);
        	JULval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("JUL").toString());
        	
        	
        	
        	
        	HSSFCell AUG = datarow.createCell(16); //8월
        	HSSFCell AUGval = datarow.createCell(17);
        	
        	Mval = Integer.parseInt(record.get("AUG").toString());
        	AUG.setCellValue(Integer.parseInt(record.get("AUG").toString()));
        	AUGval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	AUG.setCellStyle(dataStyle2);
        	AUGval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("AUG").toString());
        	
        	
        	
        	
        	HSSFCell SEP = datarow.createCell(18); //9월
        	HSSFCell SEPval = datarow.createCell(19);
        	
        	Mval = Integer.parseInt(record.get("SEP").toString());
        	SEP.setCellValue(Integer.parseInt(record.get("SEP").toString()));
        	SEPval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	SEP.setCellStyle(dataStyle2);
        	SEPval.setCellStyle(dataStyle3);
        	monthSum = monthSum +Integer.parseInt(record.get("SEP").toString());
        	
        	
        	
        	
        	HSSFCell OCT = datarow.createCell(20); //10월
        	HSSFCell OCTval = datarow.createCell(21);
        	
        	Mval = Integer.parseInt(record.get("OCT").toString());
        	OCT.setCellValue(Integer.parseInt(record.get("OCT").toString()));
        	OCTval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	OCT.setCellStyle(dataStyle2);
        	OCTval.setCellStyle(dataStyle3); 
        	monthSum = monthSum + Integer.parseInt(record.get("OCT").toString());
        	
        	
        	
        	
        	HSSFCell NOV = datarow.createCell(22); //11월
        	HSSFCell NOVval = datarow.createCell(23);
        	
        	Mval = Integer.parseInt(record.get("NOV").toString());
        	NOV.setCellValue(Integer.parseInt(record.get("NOV").toString()));
        	NOVval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	NOV.setCellStyle(dataStyle2);
        	NOVval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("NOV").toString());
        	
        	
        	
        	
        	HSSFCell DEC = datarow.createCell(24); //12월
        	HSSFCell DECval = datarow.createCell(25);
        	
        	Mval = Integer.parseInt(record.get("DEC").toString());
        	DEC.setCellValue(Integer.parseInt(record.get("DEC").toString()));
        	DECval.setCellValue(Math.round(((double) Mval / maxSend) * allPer * 100) / 100.0);
        	
        	DEC.setCellStyle(dataStyle2);
        	DECval.setCellStyle(dataStyle3);
        	monthSum = monthSum + Integer.parseInt(record.get("DEC").toString());
        	
        	
        	
        	HSSFCell allsendCell = datarow.createCell(1); // 제보건수 넣기
        	allsendCell.setCellValue(monthSum);
        	allsendCell.setCellStyle(dataStyle);
        	// 마지막 최종 제보 건수 구하기
        	allSum = allSum + monthSum;
        	rowcnt++; // 행 카운터 증가
        	Mval = 0;
        	
        	
        }
        
        CellStyle infoStyle = wb.createCellStyle();
        infoStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
		
		Font infoStyleF = wb.createFont(); // 폰트 객체 생성
		infoStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		infoStyleF.setFontName("굴림체");
		infoStyle.setFont(infoStyleF); // 폰트 스타일을 셀 스타일에 적용 
        
        
        // 총 인원수, 총 건수 넣기
        HSSFRow inforow = sheet.createRow(5);
        
        HSSFCell allInfrmCell = inforow.createCell(0); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 2)); 
        allInfrmCell.setCellValue("소속 수 : " + allInformer + "개");
        allInfrmCell.setCellStyle(infoStyle);
        
        HSSFCell allsendCell = inforow.createCell(5); // 셀 생성
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 10)); 
        allsendCell.setCellValue("총 건수 : " + allSum + "건");
        allsendCell.setCellStyle(infoStyle);
        
    }
    
    public void mileageExcel(Map model, HSSFWorkbook wb) {
    	// 굿 제보 마일리지 엑셀 다운로드
    	List mileList = (List) model.get("mileData"); // 엑셀에서 사용할 데이터 가져오기
    	
    	String mapping = (String)model.get("mapping");

    	HSSFSheet sheet;
    	if(mapping.equals("mileageExcel")) {
    		sheet = wb.createSheet("굿 제보 마일리지");
    	} else {
    		sheet = wb.createSheet("굿 제보 통신원");
    	}
    	
    	// title style 변경 
        // style 객체 생성
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 가운데 정렬 (가로 기준)
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
 	    
 	    Font font = wb.createFont(); // 폰트 객체 생성
 	    font.setFontHeightInPoints((short) 40); // 폰트 크기 설정
 	    font.setFontName("굴림체");
 	    titleStyle.setFont(font); // 폰트 스타일을 셀 스타일에 적용
    	
 	    // 엑셀 title 넣을 행 생성 (A1)
    	HSSFRow titlerow = sheet.createRow(0);
        HSSFCell titleCell = titlerow.createCell(0); // 셀 생성
    	
        // CellRangeAddress(a,b,c,d) 사용법
        // => CellRangeAddress(첫 행,마지막 행,첫 열,마지막 열)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8)); // title cell 병합 / 예제 파일 기준으로 17칸 병합
        
        String titleName = (String) model.get("titleName");
        
        titleCell.setCellValue(titleName); // title row에 title 삽입
        titleCell.setCellStyle(titleStyle); // 스타일 적용
        
        
        CellStyle headStyle = wb.createCellStyle();
     	headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 오른쪽 정렬 (가로 기준)
     	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
     	headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 배경색 설정*/ 	   
     	headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // 배경색이 채워지도록 패턴 설정
     	headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
     	headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
     	headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font headStyleF = wb.createFont(); // 폰트 객체 생성
		headStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		headStyleF.setFontName("굴림체");
		headStyle.setFont(headStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
        HSSFRow thRow = sheet.createRow(1); // th row 생성
        
        HSSFCell thCell = thRow.createCell(0);
        thCell.setCellValue("순위");
        thCell.setCellStyle(headStyle);
        
        HSSFCell thCell1 = thRow.createCell(1);
        thCell1.setCellValue("ID");
        thCell1.setCellStyle(headStyle);
        
        HSSFCell thCell2 = thRow.createCell(2);
        thCell2.setCellValue("이름");
        thCell2.setCellStyle(headStyle);
        
        HSSFCell thCell3 = thRow.createCell(3);
        thCell3.setCellValue("연락처");
        thCell3.setCellStyle(headStyle);
        
        HSSFCell thCell4 = thRow.createCell(4);
        thCell4.setCellValue("총점");
        thCell4.setCellStyle(headStyle);
        
        HSSFCell thCell5 = thRow.createCell(5);
        thCell5.setCellValue("당월 점수");
        thCell5.setCellStyle(headStyle);
        
        HSSFCell thCell6 = thRow.createCell(6);
        thCell6.setCellValue("전월 점수");
        thCell6.setCellStyle(headStyle);
        
        HSSFCell thCell7 = thRow.createCell(7);
        thCell7.setCellValue("재난");
        thCell7.setCellStyle(headStyle);
        
        HSSFCell thCell8 = thRow.createCell(8);
        thCell8.setCellValue("영상/사진");
        thCell8.setCellStyle(headStyle);
        
        
        CellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
        dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 중앙 정렬 (세로 기준)
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 테두리 설정
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		Font dataStyleF = wb.createFont(); // 폰트 객체 생성
		dataStyleF.setFontHeightInPoints((short) 14); // 폰트 크기 설정
		dataStyleF.setFontName("굴림체");
		dataStyle.setFont(dataStyleF); // 폰트 스타일을 셀 스타일에 적용 
		
        if(mileList != null) {
        	for(int i = 0; i < mileList.size(); i++) {
                HSSFRow dataRow = sheet.createRow(2+i);
                Map<String, Object> record = (Map<String, Object>) mileList.get(i);
            	
            	HSSFCell dataCell = dataRow.createCell(0);
            	int ranking = Integer.parseInt(record.get("RANKING").toString());
            	dataCell.setCellValue(ranking);
            	dataCell.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell1 = dataRow.createCell(1);
            	dataCell1.setCellValue(record.get("ACT_ID").toString());
            	dataCell1.setCellStyle(dataStyle);
            	            	
            	HSSFCell dataCell2 = dataRow.createCell(2);
            	dataCell2.setCellValue(record.get("INFORMER_NAME").toString());
            	dataCell2.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell3 = dataRow.createCell(3);
            	dataCell3.setCellValue(record.get("PHONE_CELL").toString());
            	dataCell3.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell4 = dataRow.createCell(4);
            	int allPoint = Integer.parseInt(record.get("ALL_POINT").toString());
            	dataCell4.setCellValue(allPoint);
            	dataCell4.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell5 = dataRow.createCell(5);
            	int receiptPoint = Integer.parseInt(record.get("RECEIPT_POINT").toString());
            	dataCell5.setCellValue(receiptPoint);
            	dataCell5.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell6 = dataRow.createCell(6);
            	int beforePoint = Integer.parseInt(record.get("BEFORE_POINT").toString());
            	dataCell6.setCellValue(beforePoint);
            	dataCell6.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell7 = dataRow.createCell(7);
            	int disastorPoint = Integer.parseInt(record.get("DISASTOR_POINT").toString());
            	dataCell7.setCellValue(disastorPoint);
            	dataCell7.setCellStyle(dataStyle);
            	
            	HSSFCell dataCell8 = dataRow.createCell(8);
            	int videoPoint = Integer.parseInt(record.get("VIDEO_POINT").toString());
            	dataCell8.setCellValue(videoPoint);
            	dataCell8.setCellStyle(dataStyle);
        	}
        } 
        
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
    }
    
}